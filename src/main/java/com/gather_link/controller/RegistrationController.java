package com.gather_link.controller;
import com.gather_link.model.Users;
import com.gather_link.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
public class RegistrationController {
 private final UserService users;
 public RegistrationController(UserService users){this.users=users;}
 @GetMapping("/register") public String form(){return "register";}
 @PostMapping("/register") public String register(@RequestParam String username,@RequestParam String email,@RequestParam String password,Model model){
  try {var user=new Users();user.setUsername(username);user.setEmail(email);user.setPassword(password);users.create(user);return "redirect:/login?registered";}
  catch(IllegalArgumentException e){model.addAttribute("error",e.getMessage());return "register";}
 }
}
