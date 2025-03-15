package AsukaSan.jobLancer.domain.response;

import java.time.Instant;

import AsukaSan.jobLancer.domain.response.ResponseCreUserDTO.UserOfCompany;
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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserOfCompany{
        private long id;
        private String name;
    }
}
