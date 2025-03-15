package AsukaSan.jobLancer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.domain.Job;
import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.response.ResponseCreJobDTO;
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

    @PostMapping("/jobs")
    @MessageApi("Create new Job action")
    public ResponseEntity<ResponseCreJobDTO> createNewJob(@Valid @RequestBody Job jobFromPM) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleCreate(jobFromPM));
    }
}
