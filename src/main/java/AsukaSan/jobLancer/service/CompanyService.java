package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Company;
import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.dto.Meta;
import AsukaSan.jobLancer.domain.dto.PaginationResultDTO;
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
    public PaginationResultDTO fetchAllCompanies(Pageable pageable){
        Page<Company> pageCheck = this.companyRepository.findAll(pageable);
        PaginationResultDTO res = new PaginationResultDTO();
        Meta mt = new Meta();
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
        this.companyRepository.deleteById(id);
    }
}
