package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Job;
import AsukaSan.jobLancer.domain.Skill;
import AsukaSan.jobLancer.domain.Subscriber;
import AsukaSan.jobLancer.domain.email.ResEmailJob;
import AsukaSan.jobLancer.repository.JobRepository;
import AsukaSan.jobLancer.repository.SkillRepository;
import AsukaSan.jobLancer.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final EmailService emailService;
    private final JobRepository jobRepository;
    public SubscriberService(SubscriberRepository subscriberRepository,SkillRepository skillRepository,
            EmailService emailService, JobRepository jobRepository){
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.emailService = emailService;
        this.jobRepository = jobRepository;
    }
    // # Handle on Repository
    public boolean checkEmailExist(String email){
        return this.subscriberRepository.existsByEmail(email);
    }
    public Subscriber checkById(long id){
        Optional<Subscriber> res =  this.subscriberRepository.findById(id);
        if(res.isPresent()){
            return res.get();
        }
        return null;
    }

    // # CREATE action
    public Subscriber handleCreate(Subscriber sub){
        if(sub.getSkills() != null){
            List<Long> reqSkills = sub.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> mainSkills = this.skillRepository.findByIdIn(reqSkills);
            sub.setSkills(mainSkills);
        }
        return this.subscriberRepository.save(sub);
    }
    // #UPDATE action
    public Subscriber handleUpdate(Subscriber subDatabase, Subscriber sub){
        if(sub.getSkills() != null){
            List<Long> reqSkills = sub.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> mainSkills = this.skillRepository.findByIdIn(reqSkills);
            subDatabase.setSkills(mainSkills);
        }
        return this.subscriberRepository.save(subDatabase);
    }
    // ========== Email send with user skill
    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyE(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillE> s = skills.stream().map(skill -> new ResEmailJob.SkillE(skill.getTitle()))
        .collect(Collectors.toList());
        res.setSkills(s);
        return res;
        }
       
    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
        for (Subscriber sub : listSubs) {
            List<Skill> listSkills = sub.getSkills();
            if (listSkills != null && listSkills.size() > 0) {
            List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
            if (listJobs != null && listJobs.size() > 0) {
            List<ResEmailJob> arr = listJobs.stream().map(
            job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                this.emailService.sendEmailFromTemplateSync(
                sub.getEmail(),
                "Don't miss this opportunity, apply with us now",
                "offerTemplate",
                sub.getName(),
                arr);
                    }
                }
            }
        }
    }
}
