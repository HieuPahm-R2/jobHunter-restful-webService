package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.dto.Meta;
import AsukaSan.jobLancer.domain.dto.PaginationResultDTO;
import AsukaSan.jobLancer.domain.dto.ResponseCreUserDTO;
import AsukaSan.jobLancer.domain.dto.ResponseUpdUserDTO;
import AsukaSan.jobLancer.domain.dto.ResponseUserDTO;
import AsukaSan.jobLancer.repository.UserRepository;

@Service
public class UserService {
    // DI
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User handleCreateUser(User user){
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
        Meta mt = new Meta();
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
                    item.getCreatedTime()))
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
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedTime(user.getCreatedTime());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }
    public ResponseUpdUserDTO convertToResUpdUserDTO(User user){
        ResponseUpdUserDTO res = new ResponseUpdUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedTime(user.getCreatedTime());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }
    
    public ResponseUserDTO convertToUserDTO(User user){
        ResponseUserDTO res = new ResponseUserDTO();
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
}
