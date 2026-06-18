package io.slava.usermanager.controller;

import io.slava.usermanager.dto.UserApiResponseDto;
import io.slava.usermanager.dto.UserDto;
import io.slava.usermanager.model.User;
import io.slava.usermanager.repository.RoleRepository;
import io.slava.usermanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class AdminRestController {
    private final UserService userService;

    public AdminRestController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserApiResponseDto> getAllUsers() {

        return userService.getAllUsers().stream()
                .map(user -> toResponseDto(user))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<UserApiResponseDto> createUser(@RequestBody UserDto dto) {
        User newUser = userService.addUser(dto);
        return ResponseEntity.
                created(URI.create("/api/users/" + newUser.getId()))
                .body(toResponseDto(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserApiResponseDto> updateUser(@PathVariable Long id,
                                           @RequestBody UserDto dto) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        User updatedUser = userService.updateUser(dto);
        return ResponseEntity.ok(toResponseDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private UserApiResponseDto toResponseDto(User user) {
        UserApiResponseDto userApiResponseDto = new UserApiResponseDto();
        userApiResponseDto.setId(user.getId());
        userApiResponseDto.setName(user.getName());
        userApiResponseDto.setLastName(user.getLastName());
        userApiResponseDto.setAge(user.getAge());
        userApiResponseDto.setUsername(user.getUsername());
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
        userApiResponseDto.setRoles(roleNames);
        return userApiResponseDto;
    }
}
