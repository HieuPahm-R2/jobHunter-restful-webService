package AsukaSan.jobLancer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import AsukaSan.jobLancer.domain.Job;
import AsukaSan.jobLancer.domain.Skill;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>  {
    List<Job> findBySkillsIn(List<Skill> skills); 
}
