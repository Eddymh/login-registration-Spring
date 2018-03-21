package com.eddy.springsecurity.services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eddy.springsecurity.models.Role;
import com.eddy.springsecurity.models.User;
import com.eddy.springsecurity.repositories.RoleRepository;
import com.eddy.springsecurity.repositories.UserRepository;
@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCrypt;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCrypt)     {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCrypt = bCrypt;
        
        init();
    }
    
    public void init() {
    	if(roleRepository.findAll().size() < 1 ) {		//Will check if role table is empty, if it is it will create a role for user and another for admin
    		Role user = new Role();
    		user.setName("ROLE_USER");
    		
    		Role admin = new Role();
    		admin.setName("ROLE_ADMIN");
    		
    		roleRepository.save(user);
    		roleRepository.save(admin);
    	}
    }

    // 1
    public void saveWithUserRole(User user) {
        user.setPassword(bCrypt.encode(user.getPassword()));
        user.setRoles(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }
     
     // 2 
    public void saveUserWithAdminRole(User user) {
        user.setPassword(bCrypt.encode(user.getPassword()));
        user.setRoles(roleRepository.findByName("ROLE_ADMIN"));
        userRepository.save(user);
    }    
    
    // 3
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User findByEmail(String email) {
    	return userRepository.findByEmail(email);
    }
    
    public List<User> findAll(){
    	return (List<User>) userRepository.findAll();
    }
}