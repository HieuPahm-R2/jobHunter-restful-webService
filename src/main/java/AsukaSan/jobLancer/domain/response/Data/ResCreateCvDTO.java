package AsukaSan.jobLancer.domain.response.Data;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResCreateCvDTO {
    private long id;
    private Instant createdTime;
    private String createdBy;
    
}
