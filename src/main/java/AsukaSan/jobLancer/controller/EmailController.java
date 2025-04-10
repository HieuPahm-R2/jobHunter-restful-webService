package AsukaSan.jobLancer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.service.EmailService;
import AsukaSan.jobLancer.service.SubscriberService;
import AsukaSan.jobLancer.utils.anotation.MessageApi;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;
    public EmailController(EmailService emailService, SubscriberService subscriberService){
        this.emailService= emailService;
        this.subscriberService = subscriberService;
    } 
    
    @GetMapping("/email")
    @MessageApi("send email action")
    public String handleSendEmail(){
        this.subscriberService.sendSubscribersEmailJobs();
        return "Everything done!";
    }
}
