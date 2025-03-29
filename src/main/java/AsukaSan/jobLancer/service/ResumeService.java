package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import AsukaSan.jobLancer.domain.Job;
import AsukaSan.jobLancer.domain.Resume;
import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.domain.response.Data.ResCreateCvDTO;
import AsukaSan.jobLancer.domain.response.Data.ResFetchCvDTO;
import AsukaSan.jobLancer.domain.response.Data.ResUpdateCvDTO;
import AsukaSan.jobLancer.repository.JobRepository;
import AsukaSan.jobLancer.repository.ResumeRepository;
import AsukaSan.jobLancer.repository.UserRepository;
import AsukaSan.jobLancer.utils.SecurityUtils;

@Service
public class ResumeService {
    @Autowired
    FilterBuilder fb;
    @Autowired
    private FilterParser filterParser;
    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository,JobRepository jobRepository){
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;

    }
    // Create logic
    public ResCreateCvDTO handleCreate(Resume resume){
        Resume res = this.resumeRepository.save(resume);
        ResCreateCvDTO resCreate = new ResCreateCvDTO();
        resCreate.setId(res.getId());
        resCreate.setCreatedBy(res.getCreatedBy());
        resCreate.setCreatedTime(res.getCreatedTime());
        return resCreate;
    }
    public boolean CheckUserAndJob(Resume resume){
        if(resume.getUser() == null){
            return false;
        }
        Optional<User> optionUser = this.userRepository.findById(resume.getUser().getId());
        if(optionUser.isEmpty()) return false;
        if(resume.getJob() == null) return false;
        Optional<Job> optionJob = this.jobRepository.findById(resume.getJob().getId());
        if(optionJob.isEmpty()) return false;
        return true;
    }
    //==================
    // FIND
    public Optional<Resume> findResumeById(long id){
        return this.resumeRepository.findById(id);
    }
    // Update Logic
    public Resume handleUpdate(Resume resume){
        Optional<Resume> optionRes = this.findResumeById(resume.getId());
        if(optionRes.isPresent()){
            optionRes.get().setStatus(resume.getStatus());
            this.resumeRepository.save(optionRes.get());
        }
        return null;
    }
    public ResUpdateCvDTO convertUpdateDto(Resume resume){
        ResUpdateCvDTO resUpdate = new ResUpdateCvDTO();
        resUpdate.setUpdatedBy(resume.getUpdatedBy());
        resUpdate.setUpdatedTime(resume.getUpdatedTime());
        return resUpdate;
    }
    // Delete action
    public void handleDelete(long id){
        this.resumeRepository.deleteById(id);
    }

    //Fetch only and all resume
    public ResFetchCvDTO convertFetchDto(Resume resume){
        ResFetchCvDTO res = new ResFetchCvDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setCreatedTime(resume.getCreatedTime());
        res.setCreatedBy(resume.getCreatedBy());
        res.setStatus(resume.getStatus());
        res.setUpdatedTime(resume.getUpdatedTime());
        res.setUpdatedBy(resume.getUpdatedBy());
        if(resume.getJob() != null){
            res.setCompanyName(resume.getJob().getCompany().getName());
        }

        ResFetchCvDTO.UserData userLog = new ResFetchCvDTO.UserData();
        userLog.setId(resume.getUser().getId());
        userLog.setName(resume.getUser().getName());
        res.setUser(userLog);

        ResFetchCvDTO.JobData jobLog = new ResFetchCvDTO.JobData();
        jobLog.setId(resume.getJob().getId());
        jobLog.setName(resume.getJob().getName());
        res.setJob(jobLog);
        return res;
    }
    
    public PaginationResultDTO handleFetchAll(Specification<Resume> spec,Pageable pageable){
        Page<Resume> pageCheck = this.resumeRepository.findAll(spec, pageable);

        PaginationResultDTO ans = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();

        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setTotal(pageCheck.getTotalElements());
        mt.setPages(pageCheck.getTotalPages());
        ans.setMeta(mt);

        List<ResFetchCvDTO> res = pageCheck.getContent().stream().map(item -> this.convertFetchDto(item)).collect(Collectors.toList());
        ans.setResult(res);
        return ans;
    }

    public PaginationResultDTO fetchResumesByUser(Pageable pageable){
        // Query Builder
        String email = SecurityUtils.getCurrentUserLogin().isPresent() == true ? 
                SecurityUtils.getCurrentUserLogin().get() : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageCheck = this.resumeRepository.findAll(spec, pageable);

        PaginationResultDTO ans = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();

        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setTotal(pageCheck.getTotalElements());
        mt.setPages(pageCheck.getTotalPages());
        ans.setMeta(mt);

        List<ResFetchCvDTO> res = pageCheck.getContent().stream().map(item -> this.convertFetchDto(item)).collect(Collectors.toList());
        ans.setResult(res);
        return ans;
    }
}
