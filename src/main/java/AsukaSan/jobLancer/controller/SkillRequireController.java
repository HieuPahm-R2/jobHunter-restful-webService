package AsukaSan.jobLancer.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import AsukaSan.jobLancer.domain.Job;
import AsukaSan.jobLancer.domain.Skill;
import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.service.SkillService;
import AsukaSan.jobLancer.utils.anotation.MessageApi;
import AsukaSan.jobLancer.utils.error.IdInvalidException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SkillRequireController {

    private final SkillService skillService;

    public SkillRequireController(SkillService skillService){
        this.skillService = skillService;
    }

    @PostMapping("/skills/create")
    @MessageApi("Create new skill action")
    public ResponseEntity<Skill> createNewUser(@Valid @RequestBody Skill jobFromPM) throws IdInvalidException {
        if(this.skillService.checkNameExist(jobFromPM.getTitle())){
            throw new IdInvalidException(
                "Email: " + jobFromPM.getTitle() + " đã tồn tại, hãy thử email khác"
            );
        }
        Skill accUser = this.skillService.createSkill(jobFromPM);
        return ResponseEntity.status(HttpStatus.CREATED).body(accUser);
    }
    //update
    @PostMapping("/skills")
    @MessageApi("Update skill information")
    public ResponseEntity<Skill> updateInfo(@Valid @RequestBody Skill s) throws IdInvalidException{
        Skill currentInfo = this.skillService.fetchSkillById(s.getId());
        if(currentInfo == null){
            throw new IdInvalidException("Không tồn tại kỹ năng này, hãy thử lại...");
        }
        if(s.getTitle() != null && this.skillService.checkNameExist(s.getTitle())){
            throw new IdInvalidException("Kỹ năng trên đã tồn tại, hãy thử cái mới...");
        }
        currentInfo.setTitle(s.getTitle());
        return ResponseEntity.ok().body(this.skillService.updateSkillInfo(s));
    }
    //delete
    @DeleteMapping("/skills/{id}")
    @MessageApi("Delete skill role")
    public ResponseEntity<Void> removeSkill(@PathVariable("id") long id) throws IdInvalidException{
        Skill currentInfo = this.skillService.fetchSkillById(id);
        if(currentInfo == null){
            throw new IdInvalidException("Không tồn tại kỹ năng này, hãy thử lại...");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);
    }
    //get all
     @GetMapping("/skills")
    @MessageApi("Fetch All skills action")
    public ResponseEntity<PaginationResultDTO> getAllSkillsInfo(
        @Filter Specification<Skill> spec,
        Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkills(spec,pageable)) ;
    }
}