package AsukaSan.jobLancer.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import AsukaSan.jobLancer.utils.SecurityUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="companies")
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    
    @NotBlank(message = "Name of Company not be blank")
    private String name;

    private String address;
    private String logo;

    //Fronted will handle format
    private Instant createdTime;
    private Instant updatedTime;

    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtils.getCurrentUserLogin().isPresent() == true ?
        SecurityUtils.getCurrentUserLogin().get() : " ";
        this.createdTime = Instant.now();
    }
    @PreUpdate
    public void handleBeforeUpdate() {
        this.createdBy = SecurityUtils.getCurrentUserLogin().isPresent() == true ?
        SecurityUtils.getCurrentUserLogin().get() : " ";
        this.createdTime = Instant.now();
    }
}
