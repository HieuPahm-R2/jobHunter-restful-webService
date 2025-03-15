package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.Company;
import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.domain.response.ResponseCreUserDTO;
import AsukaSan.jobLancer.domain.response.ResponseUpdUserDTO;
import AsukaSan.jobLancer.domain.response.ResponseUserDTO;
import AsukaSan.jobLancer.repository.UserRepository;

@Service
public class UserService {
    // DI
    private final UserRepository userRepository;
    private final CompanyService companyService;
    public UserService(UserRepository userRepository, CompanyService companyService){
        this.userRepository = userRepository;
        this.companyService = companyService;
    }
    public User handleCreateUser(User user){
        if(user.getCompany() != null){
            Optional<Company> comOptional = this.companyService.findCompanyById(user.getCompany().getId());
            user.setCompany(comOptional.isPresent() ? comOptional.get() : null);
        }
        return this.userRepository.save(user);
    }
    public User fetchUserById(long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
    }
    // filter and pagination
    public PaginationResultDTO fetchAllUsers(Specification<User> spec,Pageable pageable){
        Page<User> pageCheck = this.userRepository.findAll(spec, pageable);
        PaginationResultDTO res = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();
        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setPages(pageCheck.getTotalPages());
        mt.setTotal(pageCheck.getTotalElements());
        res.setMeta(mt);
        //remove sensitive data
        List<ResponseUserDTO> listUser = pageCheck.getContent()
                .stream().map(item -> new ResponseUserDTO(
                    item.getId(),
                    item.getEmail(),
                    item.getName(),
                    item.getGender(),
                    item.getAddress(),
                    item.getAge(),
                    item.getUpdatedTime(),
                    item.getCreatedTime(),
                    new ResponseUserDTO.UserOfCompany(
                        item.getCompany() != null ? item.getCompany().getId() : 0,
                        item.getCompany() != null ? item.getCompany().getName() : null
                    )
                    ))
                .collect(Collectors.toList());
        res.setResult(listUser);
        return res;
    }
    
    public void deleteUserById(long id){
        this.userRepository.deleteById(id);
    }
    // =============
    public User handleUpdateUser(User reUser){
        User currentUser = this.fetchUserById(reUser.getId());
        if(currentUser != null){
            currentUser.setAddress(reUser.getAddress());
            currentUser.setGender(reUser.getGender());
            currentUser.setName(reUser.getName());
            currentUser.setAge(reUser.getAge());
            //save to update
            currentUser = this.userRepository.save(currentUser);
            //Is the company still alive
            if(reUser.getCompany() != null){
                Optional<Company> comOptional = this.companyService.findCompanyById(reUser.getId());
                reUser.setCompany(comOptional.isPresent() ? comOptional.get() : null);
            }
        }
        return currentUser;
    }
    
    public User handleGetUserByUsername(String username){
        return this.userRepository.findByEmail(username);
    }
    // Check exception before return
    public boolean handleCheckEmailExist(String email){
        return this.userRepository.existsByEmail(email);
    }
    public boolean handleCheckIdExist(long id){
        return this.userRepository.existsById(id);
    }

    public ResponseCreUserDTO convertToResCreUserDTO(User user){
        ResponseCreUserDTO res = new ResponseCreUserDTO();
        ResponseCreUserDTO.UserOfCompany userRoot = new ResponseCreUserDTO.UserOfCompany();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedTime(user.getCreatedTime());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        if(user.getCompany() != null){
            userRoot.setId(user.getCompany().getId());
            userRoot.setName(user.getCompany().getName());
            res.setCompany(userRoot);
        }
        return res;
    }
    
    public ResponseUpdUserDTO convertToResUpdUserDTO(User user){
        ResponseUpdUserDTO.UserOfCompany userEdit = new ResponseUpdUserDTO.UserOfCompany();
        ResponseUpdUserDTO res = new ResponseUpdUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedTime(user.getCreatedTime());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        if(user.getCompany() != null){
            userEdit.setId(user.getCompany().getId());
            userEdit.setName(user.getCompany().getName());
            res.setCompany(userEdit);
        }
        return res;
    }
    
    public ResponseUserDTO convertToUserDTO(User user){
        ResponseUserDTO res = new ResponseUserDTO();
        ResponseUserDTO.UserOfCompany userRoot = new ResponseUserDTO.UserOfCompany();
        if(user.getCompany() != null){
            userRoot.setId(user.getCompany().getId());
            userRoot.setName(user.getCompany().getName());
            res.setCompany(userRoot);
        }
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedTime(user.getCreatedTime());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }
    //Refresh token
    public void SaveUserToken(String token, String email){
        User currentUser = this.handleGetUserByUsername(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
    public User fetchUserByRefreshTokenAndEmail(String token, String email){
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
