package io.slava.usermanager.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminController {

    @GetMapping
    public String index() {
        return "admin";
    }
}