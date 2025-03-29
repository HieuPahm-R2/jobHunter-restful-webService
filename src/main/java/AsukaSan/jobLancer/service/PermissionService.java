package AsukaSan.jobLancer.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Permission;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    // handle create
    public Permission handleCreate(Permission permission){
        return this.permissionRepository.save(permission);
    }
        // check valid data request is exist yet??
    public boolean alreadyExistPermission(Permission permission){
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(), permission.getApiPath(), permission.getMethod());
    }
    
    // handle update
    public Permission handleUpdate(Permission permission){
        Optional<Permission> opt = this.permissionRepository.findById(permission.getId());
        if(opt.isPresent()){
            permission.setCreatedBy(opt.get().getCreatedBy());
            permission.setCreatedTime(opt.get().getCreatedTime());
            return this.permissionRepository.save(permission);
        }
        return null;
    }
        // check Name overlap
    public boolean isEqualName(String name){
        return this.permissionRepository.existsByName(name);
    }
        // is permission already existed yet??
    public Optional<Permission> fetchPermissionById(long id){
        return this.permissionRepository.findById(id);
    }
    
    // handle Delete
    public void handleDelete(long id){
        Optional<Permission> opt = fetchPermissionById(id);
        Permission res = opt.get();
        res.getRoles().stream().forEach(item -> item.getPermissions().remove(res));
        this.permissionRepository.delete(res);
    }

    // handle filter and pagination with all items
    public PaginationResultDTO handleFetchAllPermission(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> page = this.permissionRepository.findAll(spec, pageable);

        PaginationResultDTO rs = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();

        mt.setPage(page.getNumber() + 1);
        mt.setPageSize(page.getSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(page.getContent());
        return rs;
    }
}
