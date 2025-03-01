package AsukaSan.jobLancer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.domain.Company;

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
}
