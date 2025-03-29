package AsukaSan.jobLancer.domain.response.Data;

import java.time.Instant;

import AsukaSan.jobLancer.utils.constant.ResumeStateEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResFetchCvDTO {
    private long id;
    private String email;
    private String url;

    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;

    private Instant createdTime;
    private Instant updatedTime;
    private String createdBy;
    private String updatedBy;
    private String companyName;

    private UserData user;
    private JobData job;

    @Setter
    @Getter
    public static class UserData{
        private long id;
        private String name;
    }

    @Setter
    @Getter
    public static class JobData{
        private long id;
        private String name;
    }
}
