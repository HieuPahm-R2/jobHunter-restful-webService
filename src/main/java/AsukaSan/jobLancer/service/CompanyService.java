package AsukaSan.jobLancer.service;

import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Company;
import AsukaSan.jobLancer.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company){
        return this.companyRepository.save(company);
    }
}
