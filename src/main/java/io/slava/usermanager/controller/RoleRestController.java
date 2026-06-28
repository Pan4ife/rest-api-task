package io.slava.usermanager.controller;

import io.slava.usermanager.dto.RoleDto;
import io.slava.usermanager.model.Role;
import io.slava.usermanager.repository.RoleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleRestController {

    private final RoleRepository roleRepository;

    public RoleRestController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> roleToDto(role))
                .collect(Collectors.toList());
    }

    private RoleDto roleToDto (Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        roleDto.setId(role.getId());
        return roleDto;
    }
}
