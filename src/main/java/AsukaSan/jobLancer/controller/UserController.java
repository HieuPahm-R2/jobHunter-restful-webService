package AsukaSan.jobLancer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/user/create")
    public User createNewUser(@RequestBody User userFromPostMan){
        
        User accUser = this.userService.handleCreateUser(userFromPostMan);
        return accUser;
    }
    @GetMapping("/user/{id}")
    public User getUserInfo(@PathVariable("id") long id){
        return this.userService.fetchUserById(id);
    }
    //delete user
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id){
        this.userService.deleteUserById(id);
        return "Delete completely";
    }
    @GetMapping("/user")
    public List<User> getAllUsersInfo(){
        return this.userService.fetchAllUsers();
    }
    @PutMapping("/user")
    public User getUpdateUserInfo(@RequestBody User userFromPostMan){
        return this.userService.handleCreateUser(userFromPostMan);
    }
}
