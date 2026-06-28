package io.slava.usermanager.controller;

import io.slava.usermanager.dto.UserApiResponseDto;
import io.slava.usermanager.dto.UserDto;
import io.slava.usermanager.model.User;
import io.slava.usermanager.repository.RoleRepository;
import io.slava.usermanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto dto,
                                                             BindingResult bindingResult
                                                         ) {

        if(bindingResult.hasErrors()){
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        if(userService.findByUsername(dto.getUsername()) != null){
            return ResponseEntity.badRequest().body("User with this username already exists");
        }
        User newUser = userService.addUser(dto);
        return ResponseEntity.
                created(URI.create("/api/users/" + newUser.getId()))
                .body(toResponseDto(newUser));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserApiResponseDto> getUser(@PathVariable Long id){
        User userForUpdate = userService.findById(id);
        if (userForUpdate == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toResponseDto(userForUpdate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @Valid @RequestBody UserDto dto,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        User usernameOwner = userService.findByUsername(dto.getUsername());
        dto.setId(id);
        if(usernameOwner != null && !usernameOwner.getId().equals(dto.getId())){
            return ResponseEntity.badRequest().body("User with this username already exists");
        }
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
