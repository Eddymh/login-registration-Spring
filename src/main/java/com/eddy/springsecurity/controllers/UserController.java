package com.eddy.springsecurity.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eddy.springsecurity.models.User;
import com.eddy.springsecurity.services.UserService;

@Controller
public class UserController {
    
    private UserService userService;
    
    public UserController (UserService userService) {
        this.userService = userService;
    }
    
    
    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "loginpage";
        }
        
        if ( userService.findByEmail(user.getEmail()) != null ) {
        	model.addAttribute("emailError","A user with this email already exists");
        	return "loginpage";
        }
        
        if ( userService.findByUsername(user.getUsername()) != null) {
        	model.addAttribute("usernameError", "A user with this username already exists");
        	return "loginpage";
        }
        
        if ( userService.findAll().size() < 1) {
        	userService.saveUserWithAdminRole(user);
        }
        else {
        	userService.saveWithUserRole(user);
        }
        
        return "redirect:/login";
    }
    
    @RequestMapping("/login")
    public String login(@RequestParam(value="error", 
    					required=false) String error, 
    					@RequestParam(value="logout", required=false) String logout, 
    					Model model, 
    					@Valid @ModelAttribute("user") User user) {
        if(error != null) {
            model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
        }
        if(logout != null) {
            model.addAttribute("logoutMessage", "Logout Successful!");
        }
        return "loginpage";
    }
    
    @RequestMapping(value = {"/","/home"})
    public String home(Principal principal, Model model) {
        // 1
        String username = principal.getName();
        model.addAttribute("currentUser", userService.findByUsername(username));
        return "homepage";
    }
    
    @RequestMapping("/admin")
    public String adminPage(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("currentUser", userService.findByUsername(username));
        return "adminpage";
    }
    
}