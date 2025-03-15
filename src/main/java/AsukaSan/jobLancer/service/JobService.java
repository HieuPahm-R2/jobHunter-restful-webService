package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Job;
import AsukaSan.jobLancer.domain.Skill;
import AsukaSan.jobLancer.domain.response.ResponseCreJobDTO;
import AsukaSan.jobLancer.repository.JobRepository;
import AsukaSan.jobLancer.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    public JobService(JobRepository jobRepository, SkillRepository skillRepository){
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }
    // create
    public ResponseCreJobDTO handleCreate(Job job){
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> mainSkills = this.skillRepository.findByIdList(reqSkills);
            job.setSkills(mainSkills);
        }
        //create
        Job currentJob = this.jobRepository.save(job);
        //convert
        ResponseCreJobDTO resCre = new ResponseCreJobDTO();
        resCre.setName(job.getName());
        resCre.setLocation(job.getLocation());
        resCre.setSalary(job.getSalary());
        resCre.setQuantity(job.getQuantity());
        resCre.setLevel(job.getLevel());
        resCre.setDescription(job.getDescription());
        resCre.setOpenDate(job.getOpenDate());
        resCre.setCloseDate(job.getCloseDate());
        resCre.setActive(true);
        if(currentJob.getSkills() != null){
            List<String> allSkills = currentJob.getSkills().stream().map(x -> x.getTitle()).collect(Collectors.toList());
            resCre.setSkills(allSkills);
        }
        return resCre;
    }
}
