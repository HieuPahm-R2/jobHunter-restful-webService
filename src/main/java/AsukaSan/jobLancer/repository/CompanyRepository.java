package AsukaSan.jobLancer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AsukaSan.jobLancer.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
    
}
