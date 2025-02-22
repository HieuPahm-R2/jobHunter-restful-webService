package AsukaSan.jobLancer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import AsukaSan.jobLancer.domain.User;
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
    public List<User> fetchAllUsers(){
        return this.userRepository.findAll();
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
