package AsukaSan.jobLancer.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationResultDTO {
    private Meta meta;
    private Object result;
}
