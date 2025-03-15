package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Skill;
import AsukaSan.jobLancer.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository){
        this.skillRepository = skillRepository;
    };
    // handle check before create
    public boolean checkNameExist(String name){
        return this.skillRepository.existsByName(name);
    }
    public Skill createSkill(Skill skill){
        return this.skillRepository.save(skill);
    }
    //update and find
    public Skill fetchSkillById(long id){
        Optional<Skill> sk = this.skillRepository.findById(id);
        if(sk.isPresent()){
            return sk.get();
        }
        return null;
    }
    public Skill updateSkillInfo(Skill s){
        return this.skillRepository.save(s);
    }
    //delete
    public void deleteSkill(long id){
        Optional<Skill> skOptional = this.skillRepository.findById(id);
        Skill curSkill = skOptional.get();
        curSkill.getJobs().forEach(x -> x.getSkills().remove(curSkill));
        //delete
        this.skillRepository.delete(curSkill);
    }
}
