package com.gather_link.controller;

import com.gather_link.model.Users;
import com.gather_link.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired private com.gather_link.service.UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showEditProfile(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", user);
        return "editProfile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpSession session,
                                Model model) {

        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        if(username==null || !username.matches("[A-Za-z0-9_-]{3,50}")){model.addAttribute("error","Invalid username");model.addAttribute("loggedInUser",user);return "editProfile";}
        Users existing=userRepository.findByUsername(username);
        if(existing!=null && !existing.getUserId().equals(user.getUserId())){model.addAttribute("error","Username already taken");model.addAttribute("loggedInUser",user);return "editProfile";}
        user.setUsername(username);
        if (password != null && !password.isEmpty()) {
            try {user.setPassword(userService.hashPassword(password));}
            catch(IllegalArgumentException e){model.addAttribute("error",e.getMessage());model.addAttribute("loggedInUser",user);return "editProfile";}
        }

        userRepository.save(user);
        session.setAttribute("loggedInUser", user);

        model.addAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/profile";
    }
}
