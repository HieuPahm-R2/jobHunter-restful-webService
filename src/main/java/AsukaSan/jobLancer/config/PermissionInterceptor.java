package AsukaSan.jobLancer.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import AsukaSan.jobLancer.domain.Permission;
import AsukaSan.jobLancer.domain.Role;
import AsukaSan.jobLancer.domain.User;
import AsukaSan.jobLancer.service.UserService;
import AsukaSan.jobLancer.utils.SecurityUtils;
import AsukaSan.jobLancer.utils.error.PermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PermissionInterceptor implements HandlerInterceptor{
    @Autowired UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
    HttpServletRequest request,
    HttpServletResponse response, Object handler)
    throws Exception {
    String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    String requestURI = request.getRequestURI();
    String httpMethod = request.getMethod();
    System.out.println(">>> RUN preHandle");
    System.out.println(">>> path= " + path);
    System.out.println(">>> httpMethod= " + httpMethod);
    System.out.println(">>> requestURI= " + requestURI);
    // Check Permission
    String email = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get() : "";
    if(email != null && !email.isEmpty()){
        User user = this.userService.handleGetUserByUsername(email);
        if(user != null){
            Role role = user.getRole();
            if(role != null){
                List<Permission> permissions = role.getPermissions();
                boolean isAccepted = permissions.stream().anyMatch(item -> 
                        item.getApiPath().equals(path) && item.getMethod().equals(httpMethod));
                if(isAccepted ==  false){
                    throw new PermissionException("You don't have permission to access");
                }
            }
        }
    }
    return true;
    }
   }
