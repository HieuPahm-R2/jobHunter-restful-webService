package AsukaSan.jobLancer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.service.UserService;
import AsukaSan.jobLancer.utils.error.IdInvalidException;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,  PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users/create")
    public ResponseEntity<User> createNewUser(@RequestBody User userFromPostMan){
        String hashPassword = this.passwordEncoder.encode(userFromPostMan.getPassWord());
        userFromPostMan.setPassWord(hashPassword);
        User accUser = this.userService.handleCreateUser(userFromPostMan);
        return ResponseEntity.status(HttpStatus.CREATED).body(accUser);
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserInfo(@PathVariable("id") long id){
        User fetchUser = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }
    //delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException{
        if(id > 1300){
            throw new IdInvalidException("Id limited at 1000");
        }
        this.userService.deleteUserById(id);
        return ResponseEntity.ok("Delete successfully");
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsersInfo(
        @RequestParam("currentPage") Optional<String> currentOptional,
        @RequestParam("pageSize") Optional<String> pageSizeOptional
    ){
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        int currentPage = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);
        Pageable pageable = PageRequest.of(currentPage - 1,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers(pageable)) ;
    }
    @PutMapping("/users")
    public ResponseEntity<User> getUpdateUserInfo(@RequestBody User userFromPostMan){
        
        User fetchUpdateUser = this.userService.handleUpdateUser(userFromPostMan);
        return ResponseEntity.ok(fetchUpdateUser);
    }
}
