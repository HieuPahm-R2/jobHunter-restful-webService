package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.dto.Meta;
import AsukaSan.jobLancer.domain.dto.PaginationResultDTO;
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
    public PaginationResultDTO fetchAllUsers(Specification<User> spec,Pageable pageable){
        Page<User> pageCheck = this.userRepository.findAll(spec, pageable);
        PaginationResultDTO res = new PaginationResultDTO();
        Meta mt = new Meta();
        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setPages(pageCheck.getTotalPages());
        mt.setTotal(pageCheck.getTotalElements());
        res.setMeta(mt);
        res.setResult(pageCheck.getContent());
        return res;
    }
    public void deleteUserById(long id){
        this.userRepository.deleteById(id);
    }
    public User handleUpdateUser(User reUser){
        User currentUser = this.fetchUserById(reUser.getId());
        if(currentUser != null){
            currentUser.setEmail(reUser.getEmail());
            currentUser.setName(reUser.getName());
            currentUser.setPassWord(reUser.getPassWord());
            //save to update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }
    public User handleGetUserByUsername(String username){
        return this.userRepository.findByEmail(username);
    }
}
