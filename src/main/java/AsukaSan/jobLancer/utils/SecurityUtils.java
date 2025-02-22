package AsukaSan.jobLancer.utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

@Service
public class SecurityUtils {
     public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${hieupham.jwt.base64-secret}")
    private String jwtKey;

    @Value("${hieupham.jwt.access-token-validity-in-seconds}")
    private String jwtExpiration;

   

    public void generateToken(){

    }
}
