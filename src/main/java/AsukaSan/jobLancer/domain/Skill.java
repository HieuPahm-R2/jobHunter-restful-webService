package AsukaSan.jobLancer.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import AsukaSan.jobLancer.utils.SecurityUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "skills")
@Getter
@Setter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String title;

    private Instant createdTime;
    private Instant updatedTime;
    private String createdBy;
    private String updatedBy;

    //declare relationship
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
    @JsonIgnore
    private List<Job> jobs;

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
