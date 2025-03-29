package AsukaSan.jobLancer.domain.response.Client;

import java.time.Instant;

import AsukaSan.jobLancer.domain.response.Client.ResponseCreUserDTO.UserOfCompany;
import AsukaSan.jobLancer.utils.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUpdUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private String refreshToken;
    private Instant createdTime;
    private UserOfCompany company;

    @Getter
    @Setter
    public static class UserOfCompany{
        private long id;
        private String name;
    }
}
