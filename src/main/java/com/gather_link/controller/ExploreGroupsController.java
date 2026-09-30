package com.gather_link.controller;

import com.gather_link.service.GroupMembershipService;
import com.gather_link.service.GroupService;
import com.gather_link.service.UserService;

import jakarta.servlet.http.HttpSession;

import com.gather_link.model.GroupMemberships;
import com.gather_link.model.Groups;
import com.gather_link.model.Users;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Controller
public class ExploreGroupsController {
	
	@Autowired
    private GroupService groupService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private GroupMembershipService groupMembershipService;
	
	@GetMapping("/explore-groups")
	public String showGroups(ModelMap model, HttpSession session) {
		
		if (session.getAttribute("loggedInUser") == null) {
	        return "redirect:/login";
	    }
		
        List<Groups> groups = groupService.getAllGroups();
        List<Users> users = userService.getAllUsers();
        
        
        model.addAttribute("groups", groups);
        model.addAttribute("group", new Groups());
        model.addAttribute("users", users); 
        
        return "exploreGroups";
    }
	
	@PostMapping("/groups/add")
	public String addGroup(@ModelAttribute Groups group, HttpSession session) {
	    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "redirect:/login";
	    }
	    group.setOwner(loggedInUser);
	    groupService.create(group);
	    return "redirect:/explore-groups";
	}
	
	@GetMapping("/groups/create")
	public String showCreateGroupForm(HttpSession session) {
	    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "redirect:/login";
	    }
	    return "createGroup";
	}
	
	@PostMapping("/groups/{groupId}/join")
	public String joinGroup(@PathVariable Long groupId, HttpSession session) {
	    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "redirect:/login";
	    }

	    Groups group = groupService.getByGroupId(groupId);

	    boolean alreadyJoined = false;
	    
		List<GroupMemberships> userMemberships = groupMembershipService.getMembershipsByUser(loggedInUser);
	    
	    for (GroupMemberships membership : userMemberships) {
	        if (membership.getGroup().getGroupId().equals(groupId)) {
	            alreadyJoined = true;
	            break;
	        }
	    }
	    
	    if (!alreadyJoined) {
	        groupMembershipService.addMember(group, loggedInUser);
	    }

	    return "redirect:/explore-groups";
	}

}
