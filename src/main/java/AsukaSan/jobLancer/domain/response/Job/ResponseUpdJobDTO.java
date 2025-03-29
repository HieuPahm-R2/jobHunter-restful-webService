package AsukaSan.jobLancer.domain.response.Job;

import java.time.Instant;
import java.util.List;

import AsukaSan.jobLancer.utils.constant.LevelEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUpdJobDTO {
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    //Fronted will handle format
    private Instant openDate;
    private Instant closeDate;
    private List<String> skills;
    private boolean active;
}
