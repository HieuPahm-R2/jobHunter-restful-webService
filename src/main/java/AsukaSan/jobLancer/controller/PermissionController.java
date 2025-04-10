package AsukaSan.jobLancer.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import AsukaSan.jobLancer.domain.Permission;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.service.PermissionService;
import AsukaSan.jobLancer.utils.anotation.MessageApi;
import AsukaSan.jobLancer.utils.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;
    public PermissionController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @MessageApi("Create action - permission domain")
    public ResponseEntity<Permission> handleCreate(@RequestBody Permission dataClient) throws IdInvalidException{
        if(this.permissionService.alreadyExistPermission(dataClient)){
            throw new IdInvalidException("Dữ liệu đã tồn tại, hãy thử lại với nhập liệu khác!!");
        }
        return ResponseEntity.ok().body(this.permissionService.handleCreate(dataClient));
    }

    @PutMapping("/permissions")
    @MessageApi("Update action - permission domain")
    public ResponseEntity<Permission> handleUpdate(@RequestBody Permission dataClient) throws IdInvalidException{
        if(Long.valueOf(dataClient.getId()) == null || this.permissionService.fetchPermissionById(dataClient.getId()).isEmpty()){
            if(this.permissionService.isEqualName(dataClient.getName())){
                throw new IdInvalidException("Dữ liệu xảy ra trùng lặp. Hãy thử lại!!");
            }
        }else {
            if(this.permissionService.alreadyExistPermission(dataClient)){
                throw new IdInvalidException("Dữ liệu đã tồn tại, hãy thử lại với nhập liệu khác!!");
            }else{
                return ResponseEntity.ok().body(this.permissionService.handleUpdate(dataClient));
            }
        }
        return null;
    }

    @DeleteMapping("/permissions/{id}")
    @MessageApi("Delete action - permission domain")
    public ResponseEntity<Void> handleDelete(@PathVariable("id") long id) throws IdInvalidException{
        if(this.permissionService.fetchPermissionById(id).isEmpty()){
            throw new IdInvalidException("Không tìm thấy dữ liệu hợp lệ. Hãy thử lại!!");
        }
        this.permissionService.handleDelete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/permissions")
    @MessageApi("fetch all - permission domain")
    public ResponseEntity<PaginationResultDTO> handleFetchAllPermission(
            @Filter Specification<Permission> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.permissionService.handleFetchAllPermission(spec, pageable));
    }
}
