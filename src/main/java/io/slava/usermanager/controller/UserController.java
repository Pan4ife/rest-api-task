package io.slava.usermanager.controller;

import io.slava.usermanager.model.User;
import io.slava.usermanager.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String userPage(Authentication authentication, ModelMap modelMap) {
        User user = userService.findByUsername(authentication.getName());
        modelMap.addAttribute("user", user);
        return "user";
    }
}
