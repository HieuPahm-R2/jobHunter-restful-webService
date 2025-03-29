package AsukaSan.jobLancer.domain.response.Data;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateCvDTO {
    public String updatedBy;
    public Instant updatedTime;
}
