package com.example.springapp1.controller;

import com.example.springapp1.domain.Role;
import com.example.springapp1.domain.User;
import com.example.springapp1.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MyPageController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/myPage")
    public String fakeMyPage() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepo.findByUsername(username);
        return "redirect:/myPage/" + user.getId();
    }

    @GetMapping("/myPage/{user}")
    public String myPage(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("client", userRepo.findById(user.getId()));
        return "myPage";
    }
}
