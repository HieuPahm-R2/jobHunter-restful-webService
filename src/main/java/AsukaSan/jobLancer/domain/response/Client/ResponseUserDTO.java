package AsukaSan.jobLancer.domain.response.Client;

import java.time.Instant;

import AsukaSan.jobLancer.domain.response.Client.ResponseCreUserDTO.UserOfCompany;
import AsukaSan.jobLancer.utils.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedTime;
    private Instant createdTime;
    private UserOfCompany company;
    private RoleOfUser role;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserOfCompany{
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleOfUser{
        private long id;
        private String name;
    }
}
