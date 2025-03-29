package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Permission;
import AsukaSan.jobLancer.domain.Role;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.repository.PermissionRepository;
import AsukaSan.jobLancer.repository.RoleRepository;

@Service
public class RoleService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    public RoleService(PermissionRepository permissionRepository,RoleRepository roleRepository){
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }
    // handle create
    public Role handleCreate(Role role){
        List<Long> allItems = role.getPermissions().stream().map(item -> item.getId()).collect(Collectors.toList());
        List<Permission> allPermissions = this.permissionRepository.findByIdIn(allItems);
        role.setPermissions(allPermissions);
        return this.roleRepository.save(role);
    }
    // handle update
    public Role handleUpdate(Role role){
        Optional<Role> rOptional = this.fetchRoleById(role.getId());
        if(rOptional.isPresent()){
            List<Long> allItems = role.getPermissions().stream().map(item -> item.getId()).collect(Collectors.toList());
            List<Permission> allPermissions = this.permissionRepository.findByIdIn(allItems);
            role.setPermissions(allPermissions);
            role.setCreatedBy(rOptional.get().getCreatedBy());
            role.setCreatedTime(rOptional.get().getCreatedTime());
            return this.roleRepository.save(role);
        }
        return null;
    }

    // Get role item with Id
    public Optional<Role> fetchRoleById(long id){
        return this.roleRepository.findById(id);
    }
    // Get all items with pagination and filter
    public PaginationResultDTO handleFetchAllRole(Specification<Role> spec, Pageable pageable) {
        Page<Role> page = this.roleRepository.findAll(spec, pageable);

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
    // handle delete
    public void handleDelete(long id){
        this.roleRepository.deleteById(id);
    }
}
