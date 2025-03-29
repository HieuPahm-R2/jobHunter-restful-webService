package AsukaSan.jobLancer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import AsukaSan.jobLancer.domain.Company;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.service.CompanyService;
import AsukaSan.jobLancer.utils.anotation.MessageApi;
import AsukaSan.jobLancer.utils.error.IdInvalidException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;
    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }
    
    @PostMapping("/companies")
    public ResponseEntity<?> createNewCompany(@Valid @RequestBody Company companyFromPostMan){
        
        Company newCompany = this.companyService.handleCreateCompany(companyFromPostMan);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("/company/{id}")
    @MessageApi("Fetch only company with id")
    public ResponseEntity<Company> handleFetch(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Company> resOptional = this.companyService.findCompanyById(id);
        if(resOptional == null){
            throw new IdInvalidException("Không tông tại công ty này!!");
        }
        return ResponseEntity.ok().body(resOptional.get());
    }

    @GetMapping("/companies")
    @MessageApi("Fetch all companies")
    public ResponseEntity<PaginationResultDTO > getAllCompanies(
        @Filter Specification<Company> spec, Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompanies(spec, pageable)) ;
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompanyInfo(@Valid @RequestBody Company companyFromPostMan){
        Company fetchUpdateCompany = this.companyService.handleUpdateCompany(companyFromPostMan);
        return ResponseEntity.ok(fetchUpdateCompany);
    }
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id){
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);

    }
}
