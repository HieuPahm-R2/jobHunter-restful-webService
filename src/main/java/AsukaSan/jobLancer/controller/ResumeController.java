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
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import AsukaSan.jobLancer.domain.Resume;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.domain.response.Data.ResCreateCvDTO;
import AsukaSan.jobLancer.domain.response.Data.ResFetchCvDTO;
import AsukaSan.jobLancer.domain.response.Data.ResUpdateCvDTO;
import AsukaSan.jobLancer.service.ResumeService;
import AsukaSan.jobLancer.utils.anotation.MessageApi;
import AsukaSan.jobLancer.utils.error.IdInvalidException;
import jakarta.validation.Valid;

@RestController
public class ResumeController {
    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService){
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @MessageApi("Create CV")
    public ResponseEntity<ResCreateCvDTO> handleCreate(@Valid @RequestBody Resume dataClient) throws IdInvalidException{
        if(this.resumeService.CheckUserAndJob(dataClient) == false){
            throw new IdInvalidException("Người dùng hoặc Id không hợp lệ. Hãy thử lại!");
        }
         return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleCreate(dataClient));
    }
    // Update
    @PutMapping("/jobs")
    @MessageApi("Edit and update")
    public ResponseEntity<ResUpdateCvDTO> handleUpdate(@RequestBody Resume dataClient) throws IdInvalidException{
        Optional<Resume> optionRes = this.resumeService.findResumeById(dataClient.getId());
        if(optionRes == null){
            throw new IdInvalidException("Không tồn tại thông tin nào, hãy thử lại!");
        }
        Resume resFinal = this.resumeService.handleUpdate(dataClient);
        return ResponseEntity.ok().body(this.resumeService.convertUpdateDto(resFinal));
    }
    // Delete
    @DeleteMapping("/jobs/{id}")
    @MessageApi("Delete action")
    public ResponseEntity<Void> handleDelete(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Resume> optionRes = this.resumeService.findResumeById(id);
        if(optionRes == null){
            throw new IdInvalidException("Không tồn tại thông tin nào cả. Hãy thử lại!!");
        }
        this.resumeService.handleDelete(optionRes.get().getId());
        return ResponseEntity.ok().body(null);
    }
    //===================================

    //FETCH RESUME
    @GetMapping("/jobs/{id}")
    @MessageApi("Fetch something")
    public ResponseEntity<ResFetchCvDTO> handleFetchOnly(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Resume> optional = this.resumeService.findResumeById(id);
        if (optional.isEmpty()) {
            throw new IdInvalidException("resume not found");
        }
        Resume resume = this.resumeService.findResumeById(id).get();
        return ResponseEntity.ok().body(this.resumeService.convertFetchDto(resume));
    }
    @GetMapping("/jobs")
    @MessageApi("Fetch all ")
    public ResponseEntity<PaginationResultDTO> handleFetchAll(@Filter Specification<Resume> spec, Pageable pageable){
        return ResponseEntity.ok().body(this.resumeService.handleFetchAll(spec, pageable));
    }
    @PostMapping("resumes/by-user")
    @MessageApi("Get list resumes of user")
    public ResponseEntity<PaginationResultDTO> fetchResumeByUser(Pageable pageable){
        return ResponseEntity.ok().body(this.resumeService.fetchResumesByUser(pageable));
    }

}
