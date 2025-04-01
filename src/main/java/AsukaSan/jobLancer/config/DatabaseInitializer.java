package AsukaSan.jobLancer.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Permission;
import AsukaSan.jobLancer.domain.Role;
import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.repository.PermissionRepository;
import AsukaSan.jobLancer.repository.RoleRepository;
import AsukaSan.jobLancer.repository.UserRepository;
import AsukaSan.jobLancer.utils.constant.GenderEnum;

@Service
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder){
                this.passwordEncoder = passwordEncoder;
                this.permissionRepository = permissionRepository;
                this.roleRepository = roleRepository;
                this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // TODO Auto-generated method stub
        System.out.println(">>>>>>>>> INITIAL DATABASE BEGINS:");
        long countPermission = this.permissionRepository.count();
        long countRole = this.roleRepository.count();
        long countUser = this.userRepository.count();

        if(countPermission == 0){
            ArrayList<Permission> arrResult = new ArrayList<>();
            arrResult.add(new Permission("Create new company", "/api/v1/companies","POST","COMPANIES"));
            arrResult.add(new Permission("Update a company", "/api/v1/companies","PUT","COMPANIES"));
            arrResult.add(new Permission("Delete a company", "/api/v1/companies/{id}","DELETE","COMPANIES"));
            arrResult.add(new Permission("Get a company by id", "/api/v1/companies/{id}","GET","COMPANIES"));
            arrResult.add(new Permission("Get companies with pagination", "/api/v1/companies","GET","COMPANIES"));

            arrResult.add(new Permission("Create a job", "/api/v1/jobs","POST","JOBS"));
            arrResult.add(new Permission("Update a job", "/api/v1/jobs","PUT","JOBS"));
            arrResult.add(new Permission("Delete a job", "/api/v1/jobs/{id}","DELETE","JOBS"));
            arrResult.add(new Permission("Get a job by id", "/api/v1/jobs/{id}","GET","JOBS"));
            arrResult.add(new Permission("Get jobs with pagination", "/api/v1/jobs","GET","JOBS"));

            arrResult.add(new Permission("Create a permission", "/api/v1/permissions","POST","PERMISSIONS"));
            arrResult.add(new Permission("Update a permission", "/api/v1/permissions","PUT","PERMISSIONS"));
            arrResult.add(new Permission("Delete a permission", "/api/v1/permissions/{id}","DELETE","PERMISSIONS"));
            arrResult.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}","GET","PERMISSIONS"));
            arrResult.add(new Permission("Get permission with pagination", "/api/v1/permissions","GET","PERMISSIONS"));

            arrResult.add(new Permission("Create a role", "/api/v1/roles","POST","ROLES"));
            arrResult.add(new Permission("Update a role", "/api/v1/roles","PUT","ROLES"));
            arrResult.add(new Permission("Delete a role", "/api/v1/roles/{id}","DELETE","ROLES"));
            arrResult.add(new Permission("Get a company by id", "/api/v1/roles/{id}","GET","ROLES"));
            arrResult.add(new Permission("Get roles with pagination", "/api/v1/roles","GET","ROLES"));

            arrResult.add(new Permission("Create a user", "/api/v1/users","POST","USERS"));
            arrResult.add(new Permission("Update a user", "/api/v1/users","PUT","USERS"));
            arrResult.add(new Permission("Delete a user", "/api/v1/users/{id}","DELETE","USERS"));
            arrResult.add(new Permission("Get a user by id", "/api/v1/users/{id}","GET","USERS"));
            arrResult.add(new Permission("Get users with pagination", "/api/v1/users","GET","USERS"));
            
            arrResult.add(new Permission("Create a subscriber", "/api/v1/subscribers","POST","SUBSCRIBERS"));
            arrResult.add(new Permission("Update a subscriber", "/api/v1/subscribers","PUT","SUBSCRIBERS"));
            arrResult.add(new Permission("Delete a subscriber", "/api/v1/subscribers/{id}","DELETE","SUBSCRIBERS"));
            arrResult.add(new Permission("Get a subscriber by id", "/api/v1/subscribers/{id}","GET","SUBSCRIBERS"));
            arrResult.add(new Permission("Get subscribers with pagination", "/api/v1/subscribers","GET","SUBSCRIBERS"));

            arrResult.add(new Permission("Upload file", "/api/v1/files","POST","FILES"));

            this.permissionRepository.saveAll(arrResult);
        }
        if(countRole == 0){
            List<Permission> permissions = this.permissionRepository.findAll();

            Role initRole = new Role();
            initRole.setName("MAIN_ADMIN");
            initRole.setDescription("Contain full of permissions on this web service");
            initRole.setActive(true);
            initRole.setPermissions(permissions);

            this.roleRepository.save(initRole);
        }
        if(countUser == 0){
            User initUser = new User();
            initUser.setName("Administrator H1");
            initUser.setEmail("hieu.pt@sis.hust");
            initUser.setAge(20);
            initUser.setGender(GenderEnum.MALE);
            initUser.setAddress("Thai Binh");
            initUser.setPassWord(this.passwordEncoder.encode("123456"));

            Role userRole = this.roleRepository.findByName("MAIN_ADMIN");
            if(userRole != null){
                initUser.setRole(userRole);
            }
            this.userRepository.save(initUser);
        }
        if(countRole > 0 && countPermission > 0 && countUser > 0){
            System.out.println("SKIP INITIAL DATABASE");
        }else{
            System.out.println("END TASK");
        }
    }
    
}
