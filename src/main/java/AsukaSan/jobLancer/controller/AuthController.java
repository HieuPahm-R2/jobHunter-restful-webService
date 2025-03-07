package AsukaSan.jobLancer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.dto.LoginDTO;
import AsukaSan.jobLancer.domain.dto.ResponseLoginDTO;
import AsukaSan.jobLancer.service.UserService;
import AsukaSan.jobLancer.utils.SecurityUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Value("${hieupham.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpire;

    private final SecurityUtils securityUtils;
    private final UserService userService;
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
    SecurityUtils securityUtils, UserService userService){
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //create token
        String access_token = this.securityUtils.generateAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseLoginDTO resDto = new ResponseLoginDTO();
        User userFetchDB = this.userService.handleGetUserByUsername(loginDTO.getUsername());
        if(userFetchDB != null){
            ResponseLoginDTO.LoginUser userLog = new ResponseLoginDTO.LoginUser(
                userFetchDB.getId(), userFetchDB.getEmail(), userFetchDB.getName());
            resDto.setUser(userLog);
        }
        resDto.setAccessToken(access_token);
        String refresh_token = this.securityUtils.generateRefreshToken(loginDTO.getUsername(), resDto);
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
}
