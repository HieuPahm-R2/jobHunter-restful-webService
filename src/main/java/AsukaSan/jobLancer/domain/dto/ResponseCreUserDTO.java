package AsukaSan.jobLancer.domain.dto;

import java.time.Instant;

import AsukaSan.jobLancer.utils.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCreUserDTO {

    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private String refreshToken;
    private Instant createdTime;
}
