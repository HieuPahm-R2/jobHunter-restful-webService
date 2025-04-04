package AsukaSan.jobLancer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import AsukaSan.jobLancer.domain.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill,Long>, JpaSpecificationExecutor<Skill> {
    boolean existsByTitle(String title);

    List<Skill> findByIdIn(List<Long> id);
}
