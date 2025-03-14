package AsukaSan.jobLancer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.domain.Company;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.service.CompanyService;
import jakarta.validation.Valid;

@RestController
public class CompanyController {

    private final CompanyService companyService;
    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }
    
    @PostMapping("/companies")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody Company companyFromPostMan){
        
        Company newCompany = this.companyService.handleCreateCompany(companyFromPostMan);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("/companies")
    public ResponseEntity<PaginationResultDTO > getAllUsers(
        @RequestParam("currentPage") Optional<String> currentOptional,
        @RequestParam("pageSize") Optional<String> pageSizeOptional
    ){
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        int currentPage = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);
        Pageable pageable = PageRequest.of(currentPage - 1,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompanies(pageable)) ;
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
