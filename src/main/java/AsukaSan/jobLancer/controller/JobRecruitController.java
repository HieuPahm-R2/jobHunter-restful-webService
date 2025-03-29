package AsukaSan.jobLancer.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import AsukaSan.jobLancer.domain.Job;
import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.domain.response.Job.ResponseCreJobDTO;
import AsukaSan.jobLancer.domain.response.Job.ResponseUpdJobDTO;
import AsukaSan.jobLancer.service.JobService;
import AsukaSan.jobLancer.utils.anotation.MessageApi;
import AsukaSan.jobLancer.utils.error.IdInvalidException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class JobRecruitController {
    private final JobService jobService;
    public JobRecruitController(JobService jobService){
        this.jobService = jobService;
    }
    // # create task
    @PostMapping("/jobs")
    @MessageApi("Create new Job action")
    public ResponseEntity<ResponseCreJobDTO> createNewJob(@Valid @RequestBody Job jobFromPM) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleCreate(jobFromPM));
    }
    // # update task
    @PutMapping("/jobs")
    @MessageApi("Edit job info action")
    public ResponseEntity<ResponseUpdJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException{
        Optional<Job> currentJob = this.jobService.findJobWithId(job.getId());
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Không thông tin nào về công việc này cả!");
        }
        return ResponseEntity.ok().body(this.jobService.handleUpdate(job, currentJob.get()));
    }
    // # delete task
    @DeleteMapping("/jobs/{id}")
    @MessageApi("Delete job action")
    public ResponseEntity<ResponseUpdJobDTO> deleteJob(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Job> currentJob = this.jobService.findJobWithId(id);
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Không thông tin nào về công việc này cả!");
        }
        this.jobService.handleDelete(id);
        return ResponseEntity.ok().body(null);
    }
    // # get a job with Id and fetch all jobs
    @GetMapping("/jobs/{id}")
    @MessageApi("Fetch a job action")
    public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Job> currentJob = this.jobService.findJobWithId(id);
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Không thông tin nào về công việc này cả!");
        }
        return ResponseEntity.ok().body(currentJob.get());
    }
    @GetMapping("/jobs")
    @MessageApi("Get all jobs action")
    public ResponseEntity<PaginationResultDTO> getAllJobs(@Filter Specification<Job> spec, Pageable pageable){
        return ResponseEntity.ok().body(this.jobService.fetchAllJobs(spec, pageable));
    }
    
}
