package AsukaSan.jobLancer.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class ResponseLoginDTO {
    @JsonProperty("access_token")
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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetAccountUser {
        private LoginUser user;
    }
}
