package AsukaSan.jobLancer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.domain.Subscriber;
import AsukaSan.jobLancer.service.SubscriberService;
import AsukaSan.jobLancer.utils.SecurityUtils;
import AsukaSan.jobLancer.utils.anotation.MessageApi;
import AsukaSan.jobLancer.utils.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService){
        this.subscriberService = subscriberService;
    }
    @PostMapping("/subscribers")
    @MessageApi("Create new action - subscriber module")
    public ResponseEntity<Subscriber> handleCreate(@RequestBody Subscriber dataClient) throws IdInvalidException{
        if(this.subscriberService.checkEmailExist(dataClient.getEmail())){
            throw new IdInvalidException("Email already exists, please try again!!");
        }
        return ResponseEntity.ok().body(this.subscriberService.handleCreate(dataClient));
    }
    @PutMapping("/subscribers")
    @MessageApi("Update action - subscriber module")
    public ResponseEntity<Subscriber> handleUpdate(@RequestBody Subscriber dataClient) throws IdInvalidException{
        Subscriber subOnDB = this.subscriberService.checkById(dataClient.getId());
        if(subOnDB == null){
            throw new IdInvalidException("Not exist any information about data over there");
        }
        return ResponseEntity.ok().body(this.subscriberService.handleUpdate(subOnDB, dataClient));
    }
    // Get skills register
    @PostMapping("/subscribers/skills")
    @MessageApi("Get Subscribers' skills")
    public ResponseEntity<Subscriber> getSubscriberSkill() throws IdInvalidException{
        String email = SecurityUtils.getCurrentUserLogin().isPresent() == true ? 
                SecurityUtils.getCurrentUserLogin().get() : "";
        return ResponseEntity.ok().body(this.subscriberService.findWithEmail(email));
    }
}
