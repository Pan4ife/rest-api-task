package io.slava.usermanager.controller;

import io.slava.usermanager.dto.UserEditDto;
import io.slava.usermanager.model.Role;
import io.slava.usermanager.model.User;
import io.slava.usermanager.repository.RoleRepository;
import io.slava.usermanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        List<User> users = userService.getAllUsers();
        modelMap.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/new")
    public String newUser(ModelMap modelMap) {
        User newUser = new User();
        modelMap.addAttribute("user", newUser);
        modelMap.addAttribute("allRoles", roleRepository.findAll());
        return "new-user";
    }

    @PostMapping
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                             ModelMap modelMap) {
        if (roleIds == null || roleIds.isEmpty()) {
            bindingResult.reject("noRoles", "Необходимо выбрать хотя бы одну роль");
        }
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("allRoles", roleRepository.findAll());
            return "new-user";
        }
        userService.addUser(user, roleIds);
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editedUser(@PathVariable("id") Long id, ModelMap modelMap) {
        User editUser = userService.findById(id);
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setId(editUser.getId());
        userEditDto.setName(editUser.getName());
        userEditDto.setLastName(editUser.getLastName());
        userEditDto.setAge(editUser.getAge());
        userEditDto.setUsername(editUser.getUsername());
        Set<Long> currentRoleIds = editUser.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
        userEditDto.setRoleIds(currentRoleIds);
        modelMap.addAttribute("user", userEditDto);
        modelMap.addAttribute("allRoles", roleRepository.findAll());
        return "edit-user";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute("user") @Valid UserEditDto dto,
                             BindingResult bindingResult,
                             ModelMap modelMap) {
        dto.setId(id);
        if (dto.getRoleIds() == null || dto.getRoleIds().isEmpty()) {
            bindingResult.reject("noRoles", "Необходимо выбрать хотя бы одну роль");
        }
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("allRoles", roleRepository.findAll());
            return "edit-user";
        }
        userService.updateUser(dto);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

}