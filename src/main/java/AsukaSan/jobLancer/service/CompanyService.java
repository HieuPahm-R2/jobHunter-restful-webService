package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import AsukaSan.jobLancer.config.JWTConfiguration;
import AsukaSan.jobLancer.domain.Company;
import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.repository.CompanyRepository;
import AsukaSan.jobLancer.repository.UserRepository;

@Service
public class CompanyService {

    private final JWTConfiguration JWTConfiguration;

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    public CompanyService(CompanyRepository companyRepository, JWTConfiguration JWTConfiguration
    ,UserRepository userRepository){
        this.companyRepository = companyRepository;
        this.JWTConfiguration = JWTConfiguration;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company company){
        return this.companyRepository.save(company);
    }
    public PaginationResultDTO fetchAllCompanies(Pageable pageable){
        Page<Company> pageCheck = this.companyRepository.findAll(pageable);
        PaginationResultDTO res = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();
        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setPages(pageCheck.getTotalPages());
        mt.setTotal(pageCheck.getTotalElements());
        res.setMeta(mt);
        res.setResult(pageCheck.getContent());
        return res;
    }
    public Company handleUpdateCompany(Company reCompany){
        Optional<Company> comOptional = this.companyRepository.findById(reCompany.getId());
        if(comOptional.isPresent()){
            Company curCompany = comOptional.get();
            curCompany.setLogo(reCompany.getLogo());
            curCompany.setName(reCompany.getName());
            curCompany.setDescription(reCompany.getDescription());
            curCompany.setAddress(reCompany.getAddress());
            return this.companyRepository.save(curCompany);
        }
        return null;
    }
    public void handleDeleteCompany(long id){
        Optional<Company> currentCompany = findCompanyById(id);
        if(currentCompany.isPresent()){
            Company companyDelete = currentCompany.get();
            List<User> allUsers = this.userRepository.findByCompany(companyDelete);
            this.userRepository.deleteAll(allUsers);
        }
        this.companyRepository.deleteById(id);
    }
    public Optional<Company> findCompanyById(long id){
        return this.companyRepository.findById(id);
    }
}
