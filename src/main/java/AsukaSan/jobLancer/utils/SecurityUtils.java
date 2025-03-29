package AsukaSan.jobLancer.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

import AsukaSan.jobLancer.domain.response.ResponseLoginDTO;

@Service
public class SecurityUtils {
    private final JwtEncoder jwtEncoder;

    public SecurityUtils(JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${hieupham.jwt.base64-secret}")
    private String jwtKey;

    @Value("${hieupham.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpire;
    @Value("${hieupham.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpire;

   

    public String generateAccessToken(String email, ResponseLoginDTO dto){
        // assign to claim
        ResponseLoginDTO.UserInfoInsideToken tokenUser = new ResponseLoginDTO.UserInfoInsideToken();
        tokenUser.setId(dto.getUser().getId());
        tokenUser.setEmail(dto.getUser().getEmail());
        tokenUser.setName(dto.getUser().getName());

        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpire, ChronoUnit.SECONDS);
 
        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(email)
        .claim("user account", tokenUser)
        .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String generateRefreshToken(String email, ResponseLoginDTO dataDto){
        ResponseLoginDTO.UserInfoInsideToken tokenUser = new ResponseLoginDTO.UserInfoInsideToken();
        tokenUser.setId(dataDto.getUser().getId());
        tokenUser.setEmail(dataDto.getUser().getEmail());
        tokenUser.setName(dataDto.getUser().getName());

        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpire, ChronoUnit.SECONDS);

 
        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(email)
        .claim("user", tokenUser)
        .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

     /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
    //
    public SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
    public Jwt confirmValidRefreshToken(String token){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> Refresh_token gets error: " + e.getMessage());
            throw e;
        }
    }
}
