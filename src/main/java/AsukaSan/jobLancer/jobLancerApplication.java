package AsukaSan.jobLancer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// (exclude = {
//     org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//     org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
// })
@SpringBootApplication
public class jobLancerApplication {
    public static void main(String[] args) {
        SpringApplication.run(jobLancerApplication.class, args);
    }
}
