package AsukaSan.jobLancer.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import AsukaSan.jobLancer.domain.Role;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.repository.RoleRepository;
import AsukaSan.jobLancer.service.RoleService;
import AsukaSan.jobLancer.utils.anotation.MessageApi;
import AsukaSan.jobLancer.utils.error.IdInvalidException;

@RestController
public class RoleController {
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public RoleController(RoleRepository roleRepository, RoleService roleService){
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }
    
    @PostMapping("/roles")
    @MessageApi("Create role action")
    public ResponseEntity<Role> handleCreate(@RequestBody Role dataClient) throws IdInvalidException{
        if(this.roleRepository.existsByName(dataClient.getName())){
            throw new IdInvalidException("Role đã tồn tại!!");
        }
        return ResponseEntity.ok().body(this.roleService.handleCreate(dataClient));
    }

    @PutMapping("/roles/{id}")
    @MessageApi("Update role action")
    public ResponseEntity<Role> handleUpdate(@RequestBody Role dataClient) throws IdInvalidException{
        if(Long.valueOf(dataClient.getId()) == null || this.roleService.fetchRoleById(dataClient.getId()).isEmpty()){
            throw new IdInvalidException("Không tìm thấy bất kỳ thông tin nào, kiểm tra lại?");
        }
        return ResponseEntity.ok().body(this.roleService.handleUpdate(dataClient));
    }

    @DeleteMapping("/roles/{id}")
    @MessageApi("Delete item action - role domain")
    public ResponseEntity<Void> handleDelete(@PathVariable("id") long id) throws IdInvalidException{
        if(this.roleService.fetchRoleById(id).isEmpty()){
            throw new IdInvalidException("Không tìm thấy dữ liệu hợp lệ. Hãy thử lại!!");
        }
        this.roleService.handleDelete(id);
        return ResponseEntity.ok().body(null);
    }
    // FETCH SINGLE ITEM AND ALL ITEMS
    @GetMapping("/roles")
    @MessageApi("fetch all action - role domain")
    public ResponseEntity<PaginationResultDTO> handleFetchAllRole(
            @Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.handleFetchAllRole(spec, pageable));
    }
    @GetMapping("/roles/{id}")
    @MessageApi("fetch single item action - role domain")
    public ResponseEntity<Role> handleFetchSingle(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Role> optRes = this.roleService.fetchRoleById(id);
        if(optRes.isEmpty()){
            throw new IdInvalidException("Không tìm thấy dữ liệu hợp lệ. Hãy thử lại!!");
        }
        return ResponseEntity.ok().body(optRes.get());
    }
}
