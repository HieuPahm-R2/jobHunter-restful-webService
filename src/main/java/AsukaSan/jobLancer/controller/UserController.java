package AsukaSan.jobLancer.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;

import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.domain.response.PaginationResultDTO;
import AsukaSan.jobLancer.domain.response.Client.ResponseCreUserDTO;
import AsukaSan.jobLancer.domain.response.Client.ResponseUpdUserDTO;
import AsukaSan.jobLancer.domain.response.Client.ResponseUserDTO;
import AsukaSan.jobLancer.service.UserService;
import AsukaSan.jobLancer.utils.anotation.MessageApi;
import AsukaSan.jobLancer.utils.error.IdInvalidException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,  PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users/create")
    @MessageApi("Create new User action")
    public ResponseEntity<ResponseCreUserDTO> createNewUser(@Valid @RequestBody User userFromPostMan) throws IdInvalidException {
        if(this.userService.handleCheckEmailExist(userFromPostMan.getEmail())){
            throw new IdInvalidException(
                "Email: " + userFromPostMan.getEmail() + " đã tồn tại, hãy thử email khác"
            );
        }
        String hashPassword = this.passwordEncoder.encode(userFromPostMan.getPassWord());
        userFromPostMan.setPassWord(hashPassword);
        User accUser = this.userService.handleCreateUser(userFromPostMan);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreUserDTO(accUser));
    }
    @GetMapping("/users/{id}")
    @MessageApi("Watch User information action")
    public ResponseEntity<ResponseUserDTO> getUserInfo(@PathVariable("id") long id) throws IdInvalidException{
        if(!this.userService.handleCheckIdExist(id)){
            throw new IdInvalidException("Không tồn tại người dùng này, hãy kiểm tra lại!");
        }
        User fetchUser = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToUserDTO(fetchUser));
    }
    //delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException{
        User currentUser = this.userService.fetchUserById(id);
        if(currentUser == null){
            throw new IdInvalidException("Người dùng không tồn tại, hãy kiểm tra lại!");
        }
        this.userService.deleteUserById(id);
        return ResponseEntity.ok(null);
    }
    //get all users
    @GetMapping("/users")
    @MessageApi("Fetch All Users action")
    public ResponseEntity<PaginationResultDTO> getAllUsersInfo(
        @Filter Specification<User> spec,
        Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers(spec,pageable)) ;
    }
    @PutMapping("/users")
    @MessageApi("Update user information action")
    public ResponseEntity<ResponseUpdUserDTO> getUpdateUserInfo(@Valid @RequestBody User userFromPostMan) throws IdInvalidException{
        if(!this.userService.handleCheckIdExist(userFromPostMan.getId())){
            throw new IdInvalidException("Id không tồn tại, vui lòng kiểm tra lại");
        }
        User fetchUpdateUser = this.userService.handleUpdateUser(userFromPostMan);
        
        return ResponseEntity.ok(this.userService.convertToResUpdUserDTO(fetchUpdateUser));
    }
}
