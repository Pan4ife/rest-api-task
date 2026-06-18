package io.slava.usermanager.controller;

import io.slava.usermanager.dto.UserDto;
import io.slava.usermanager.model.User;
import io.slava.usermanager.repository.RoleRepository;
import io.slava.usermanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/admin/users")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String index(ModelMap modelMap) {
        String activeTab = "all-users";
        List<User> users = userService.getAllUsers();
        User newUser = new User();
        modelMap.addAttribute("activeTab", activeTab);
        modelMap.addAttribute("users", users);
        modelMap.addAttribute("user", newUser);
        modelMap.addAttribute("allRoles", roleRepository.findAll());
        return "admin";
    }


//    @PostMapping
//    public String createUser(@ModelAttribute("user") @Valid User user,
//                             BindingResult bindingResult,
//                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
//                             ModelMap modelMap) {
//        String activeTab = "new-user";
//        if (roleIds == null || roleIds.isEmpty()) {
//            bindingResult.reject("noRoles", "At least one role must be selected");
//        }
//        if (userService.findByUsername(user.getUsername()) != null) {
//            bindingResult.rejectValue("username", "duplicate", "User with this username already exists");
//        }
//        if (bindingResult.hasErrors()) {
//            List<User> users = userService.getAllUsers();
//            modelMap.addAttribute("activeTab", activeTab);
//            modelMap.addAttribute("users", users);
//            modelMap.addAttribute("allRoles", roleRepository.findAll());
//            return "admin";
//        }
//        userService.addUser(user    );
//        return "redirect:/admin/users";
//    }
//
//
//    @PostMapping("/{id}")
//    public String updateUser(@PathVariable("id") Long id,
//                             @ModelAttribute("user") @Valid UserDto dto,
//                             BindingResult bindingResult,
//                             RedirectAttributes redirectAttributes) {
//        User usernameOwner = userService.findByUsername(dto.getUsername());
//        dto.setId(id);
//        if (dto.getRoleIds() == null || dto.getRoleIds().isEmpty()) {
//            bindingResult.reject("noRoles", "At least one role must be selected");
//        }
//        if (usernameOwner != null && !usernameOwner.getId().equals(dto.getId())) {
//            redirectAttributes.addFlashAttribute("usernameError", "User with this username already exists");
//            return "redirect:/admin/users";
//        }
//        if (bindingResult.hasErrors()) {
//            return "redirect:/admin/users";
//        }
//        userService.updateUser(dto);
//        return "redirect:/admin/users";
//    }
//
//    @PostMapping("/{id}/delete")
//    public String deleteUser(@PathVariable("id") Long id) {
//        userService.deleteById(id);
//        return "redirect:/admin/users";
//    }

}