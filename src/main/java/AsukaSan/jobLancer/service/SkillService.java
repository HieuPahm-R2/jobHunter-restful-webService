package AsukaSan.jobLancer.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Skill;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository){
        this.skillRepository = skillRepository;
    };
    // handle check before create
    public boolean checkNameExist(String name){
        return this.skillRepository.existsByTitle(name);
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
    // #delete
    public void deleteSkill(long id){
        Optional<Skill> skOptional = this.skillRepository.findById(id);
        Skill curSkill = skOptional.get();
        curSkill.getJobs().forEach(x -> x.getSkills().remove(curSkill));
        //delete
        this.skillRepository.delete(curSkill);
    }
    // # get all action
    public PaginationResultDTO fetchAllSkills(Specification<Skill> spec,Pageable pageable){
        Page<Skill> pageCheck = this.skillRepository.findAll(spec, pageable);
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
