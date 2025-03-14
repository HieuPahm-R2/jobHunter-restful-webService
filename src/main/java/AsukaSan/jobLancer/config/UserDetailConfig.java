package AsukaSan.jobLancer.config;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import AsukaSan.jobLancer.service.UserService;

@Component("userDetailsService")
public class UserDetailConfig implements UserDetailsService {
    private final UserService userService;
    public UserDetailConfig(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AsukaSan.jobLancer.domain.User user = this.userService.handleGetUserByUsername(username);
        return new User(user.getEmail(), user.getPassWord(), Collections.singletonList(new SimpleGrantedAuthority("ROLE-User")));
    }
    
}
