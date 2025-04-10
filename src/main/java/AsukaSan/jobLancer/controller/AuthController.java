package AsukaSan.jobLancer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.request.RequestLoginDTO;
import AsukaSan.jobLancer.domain.response.ResponseLoginDTO;
import AsukaSan.jobLancer.domain.response.Client.ResponseCreUserDTO;
import AsukaSan.jobLancer.service.UserService;
import AsukaSan.jobLancer.utils.SecurityUtils;
import AsukaSan.jobLancer.utils.anotation.MessageApi;
import AsukaSan.jobLancer.utils.error.IdInvalidException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Value("${hieupham.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpire;

    private final SecurityUtils securityUtils;
    private final UserService userService;
    private AuthenticationManagerBuilder authenticationManagerBuilder;
        private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
    SecurityUtils securityUtils, UserService userService,PasswordEncoder passwordEncoder){
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody RequestLoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //create token
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseLoginDTO resDto = new ResponseLoginDTO();
        User userFetchDB = this.userService.handleGetUserByUsername(loginDTO.getUsername());
        if(userFetchDB != null){
            ResponseLoginDTO.LoginUser userLog = new ResponseLoginDTO.LoginUser(
                userFetchDB.getId(), 
                userFetchDB.getEmail(), 
                userFetchDB.getName(), 
                userFetchDB.getRole());
            resDto.setUser(userLog);
        }
        String access_token = this.securityUtils.generateAccessToken(authentication.getName(), resDto);
        
        resDto.setAccessToken(access_token);
        String refresh_token = this.securityUtils.generateRefreshToken(loginDTO.getUsername(), resDto);
        //save the token on db
        this.userService.SaveUserToken(refresh_token, loginDTO.getUsername());
        //setup cookies
        ResponseCookie resCookies = ResponseCookie
                    .from("refresh-token", refresh_token)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(refreshTokenExpire)
                    .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE , resCookies.toString())
            .body(resDto);
    }
    //Generate Token
    @GetMapping("/auth/account")
    @MessageApi("fetch account")
    public ResponseEntity<ResponseLoginDTO.GetAccountUser> getAccountLogin(){
        String emailSaved = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User userFetchDB = this.userService.handleGetUserByUsername(emailSaved);
        ResponseLoginDTO.LoginUser userLog = new ResponseLoginDTO.LoginUser();
        ResponseLoginDTO.GetAccountUser getAccountUser = new ResponseLoginDTO.GetAccountUser();
        if(userFetchDB != null){
             userLog.setId(userFetchDB.getId());
             userLog.setEmail(userFetchDB.getEmail());
             userLog.setName(userFetchDB.getName());
             userLog.setRole(userFetchDB.getRole());
             getAccountUser.setUser(userLog);
        }
        return ResponseEntity.ok().body(getAccountUser);
    }

    @GetMapping("/auth/refresh")
    @MessageApi("Get User by refresh token")
    public  ResponseEntity<ResponseLoginDTO> getRefreshToken(@CookieValue(name="refresh_token",  defaultValue = "abc")String refreshToken) throws IdInvalidException{
        if (refreshToken.equals("abc")) {
            throw new IdInvalidException("Bạn không có refresh token ở cookie");
        }
        // running check valid
        Jwt correctToken = this.securityUtils.confirmValidRefreshToken(refreshToken);
        String email = correctToken.getSubject();
        User currentUser = this.userService.fetchUserByRefreshTokenAndEmail(refreshToken, email);
        if(currentUser == null){
            throw new IdInvalidException("Not valid refresh token");
        }

        ResponseLoginDTO resDto = new ResponseLoginDTO();
        User userFetchDB = this.userService.handleGetUserByUsername(email);
        if(userFetchDB != null){
            ResponseLoginDTO.LoginUser userLog = new ResponseLoginDTO.LoginUser(
                userFetchDB.getId(), userFetchDB.getEmail(), userFetchDB.getName(), userFetchDB.getRole());
            resDto.setUser(userLog);
        }
        String access_token = this.securityUtils.generateAccessToken(email, resDto);

        resDto.setAccessToken(access_token);
        // generate again refresh token
        String reload_refresh_token = this.securityUtils.generateRefreshToken(email, resDto);
        // save and equal data in db
        this.userService.SaveUserToken(reload_refresh_token, email);
        //setup cookies
        ResponseCookie resCookies = ResponseCookie
                    .from("refresh-token", reload_refresh_token)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(refreshTokenExpire)
                    .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE , resCookies.toString())
            .body(resDto);
    }

    //Logout API 
    @PostMapping("/auth/logout")
    @MessageApi("Logout with user action")
    public ResponseEntity<Void> goLogout() throws IdInvalidException{
        String emailUser = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        if(emailUser.equals("")){
            throw new IdInvalidException("not valid access token");
        }
        //update refresh token into NULL
        this.userService.SaveUserToken(null, emailUser);
        ResponseCookie removeSpringCookies = ResponseCookie
        .from("refresh-token", null)
        .httpOnly(true)
        .path("/")
        .maxAge(0)
        .build();
        return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE , removeSpringCookies.toString())
        .body(null);
    }
    // Register action
    @PostMapping("/auth/register")
    @MessageApi("Create new User action")
    public ResponseEntity<ResponseCreUserDTO> registerHandle(@Valid @RequestBody User userFromPostMan) throws IdInvalidException {
        if(this.userService.handleCheckEmailExist(userFromPostMan.getEmail())){
            throw new IdInvalidException(
                "Email: " + userFromPostMan.getEmail() + " đã tồn tại, hãy thử email khác"
            );
        }
        String hashPassword = this.passwordEncoder.encode(userFromPostMan.getPassword());
        userFromPostMan.setPassword(hashPassword);
        User regisUser = this.userService.handleCreateUser(userFromPostMan);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreUserDTO(regisUser));
    }
}
