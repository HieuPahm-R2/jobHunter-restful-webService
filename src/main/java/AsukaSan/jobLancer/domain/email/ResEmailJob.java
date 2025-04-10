package AsukaSan.jobLancer.domain.email;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResEmailJob {
    private double salary;
    private String name;
    private CompanyE company;
    private List<SkillE> skills;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CompanyE{
        private String name;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SkillE{
        private String title;
    }
}
