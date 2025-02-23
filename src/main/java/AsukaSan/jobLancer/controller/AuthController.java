package AsukaSan.jobLancer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.domain.dto.LoginDTO;
import AsukaSan.jobLancer.domain.dto.ResponseLoginDTO;
import AsukaSan.jobLancer.utils.SecurityUtils;
import jakarta.validation.Valid;

@RestController
public class AuthController {
    
    private final SecurityUtils securityUtils;
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
    SecurityUtils securityUtils){
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //create token
        String access_token = this.securityUtils.generateToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseLoginDTO resDto = new ResponseLoginDTO();
        resDto.setAccessToken(access_token);
        return ResponseEntity.ok().body(resDto);
    }
    //Generate Token
}
