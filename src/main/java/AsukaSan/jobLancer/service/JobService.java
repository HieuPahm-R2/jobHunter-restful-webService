package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Company;
import AsukaSan.jobLancer.domain.Job;
import AsukaSan.jobLancer.domain.Skill;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.domain.response.Job.ResponseCreJobDTO;
import AsukaSan.jobLancer.domain.response.Job.ResponseUpdJobDTO;
import AsukaSan.jobLancer.repository.CompanyRepository;
import AsukaSan.jobLancer.repository.JobRepository;
import AsukaSan.jobLancer.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;
    public JobService(JobRepository jobRepository, SkillRepository skillRepository,CompanyRepository companyRepository){
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }
    //find job with Id
    public Optional<Job> findJobWithId(long id){
        return this.jobRepository.findById(id);
    }
    // #create task
    public ResponseCreJobDTO handleCreate(Job job){
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> mainSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(mainSkills);
        }
        if(job.getCompany() != null){
            Optional<Company> resOptional = this.companyRepository.findById(job.getCompany().getId());
            if(resOptional.isPresent()){
                job.setCompany(resOptional.get());
            }
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
    // # update task
    public ResponseUpdJobDTO handleUpdate(Job job, Job mainJob){
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> mainSkills = this.skillRepository.findByIdIn(reqSkills);
            mainJob.setSkills(mainSkills);
        }
        // Is the company exist??
        if(job.getCompany() != null){
            Optional<Company> resOptional = this.companyRepository.findById(job.getCompany().getId());
            if(resOptional.isPresent()){
                mainJob.setCompany(resOptional.get());
            }
        }
        // SAVE correct information
        mainJob.setName(job.getName());
        mainJob.setSalary(job.getSalary());
        mainJob.setQuantity(job.getQuantity());
        mainJob.setLevel(job.getLevel());
        mainJob.setDescription(job.getDescription());
        mainJob.setOpenDate(job.getOpenDate());
        mainJob.setCloseDate(job.getCloseDate());
        mainJob.setActive(job.isActive());
        //create
        Job currentJob = this.jobRepository.save(mainJob);
        //convert
        ResponseUpdJobDTO resUpd = new ResponseUpdJobDTO();
        resUpd.setName(job.getName());
        resUpd.setLocation(job.getLocation());
        resUpd.setSalary(job.getSalary());
        resUpd.setQuantity(job.getQuantity());
        resUpd.setLevel(job.getLevel());
        resUpd.setDescription(job.getDescription());
        resUpd.setOpenDate(job.getOpenDate());
        resUpd.setCloseDate(job.getCloseDate());
        resUpd.setActive(true);
        if(currentJob.getSkills() != null){
            List<String> allSkills = currentJob.getSkills().stream().map(x -> x.getTitle()).collect(Collectors.toList());
            resUpd.setSkills(allSkills);
        }
        return resUpd;
    }
    // # delete task
    public void handleDelete(long id){
        this.jobRepository.deleteById(id);
    }
    // # get all jobs
     public PaginationResultDTO fetchAllJobs(Specification<Job> spec,Pageable pageable){
        Page<Job> pageCheck = this.jobRepository.findAll(spec, pageable);
        PaginationResultDTO res = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();
        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setPages(pageCheck.getTotalPages());
        mt.setTotal(pageCheck.getTotalElements());
        res.setMeta(mt);
        //remove sensitive data
        res.setResult(pageCheck.getContent());
        return res;
    }
}
