package AsukaSan.jobLancer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class ResponseLoginDTO {
    private String accessToken;
    private LoginUser user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUser {
        private long id;
        private String email;
        private String name;
    }
}
