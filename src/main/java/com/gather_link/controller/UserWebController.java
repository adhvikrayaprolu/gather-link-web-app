package com.gather_link.controller;

import com.gather_link.model.GroupMemberships;
import com.gather_link.model.Users;
import com.gather_link.service.GroupMembershipService;
import com.gather_link.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserWebController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private GroupMembershipService groupMembershipService;

    @GetMapping("/users")
    public String showUsers(ModelMap modelMap, HttpSession session) {
    	
    	if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
    	
    	List<Users> allUsers = userService.getAllUsers();
    	modelMap.addAttribute("users", allUsers);
        modelMap.addAttribute("user", new Users());
        
        Map<Long, List<String>> userGroupsMap = new HashMap<>();
        for (Users user : allUsers) {
            List<GroupMemberships> memberships = groupMembershipService.getMembershipsByUser(user);
            List<String> groupNames = new ArrayList<>();

            for (GroupMemberships membership : memberships) {
                groupNames.add(membership.getGroup().getGroupName());
            }

            userGroupsMap.put(user.getUserId(), groupNames);
        }
        
        modelMap.addAttribute("userGroupsMap", userGroupsMap);
        
        return "users";
    }
    
}
