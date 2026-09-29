package com.gather_link.controller;

import com.gather_link.model.Groups;
import com.gather_link.model.Posts;
import com.gather_link.model.Users;
import com.gather_link.service.GroupService;
import com.gather_link.service.PostService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.format.DateTimeFormatter;

@Controller
public class PostPageController {
 @Autowired private com.gather_link.service.GroupAccess access;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PostService postService;

    @GetMapping("/posts/group/{groupId}")
    public String viewPosts(@PathVariable("groupId") Long groupId, Model model, HttpSession session) {
        Groups group = groupService.getByGroupId(groupId);
        access.requireMember(group, (Users) session.getAttribute("loggedInUser"));
        List<Posts> posts = postService.getByGroupId(groupId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        for (Posts post : posts) {
            String formatted = post.getPostCreation().format(formatter);
            post.setFormattedDate(formatted);
        }

        model.addAttribute("group", group);
        model.addAttribute("posts", posts);

        return "posts";
    }

    @GetMapping("/posts/create")
    public String showCreatePostForm(@RequestParam("groupId") Long groupId, Model model, HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Groups group = groupService.getByGroupId(groupId);
        access.requireMember(group, (Users) session.getAttribute("loggedInUser"));
        model.addAttribute("group", group);
        return "createPost";
    }

    @PostMapping("/posts/create")
    public String handleCreatePost(@RequestParam("groupId") Long groupId,
                                   @RequestParam("content") String content,
                                   HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Groups group = groupService.getByGroupId(groupId);
        access.requireMember(group, (Users) session.getAttribute("loggedInUser"));
        Posts post = new Posts();
        post.setGroup(group);
        post.setPostCreator(loggedInUser);
        post.setContent(content);

        postService.create(post);

        return "redirect:/posts/group/" + groupId;
    }

}
