package io.slava.usermanager.service;

import io.slava.usermanager.dto.UserDto;
import io.slava.usermanager.model.Role;
import io.slava.usermanager.model.User;
import io.slava.usermanager.repository.RoleRepository;
import io.slava.usermanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User addUser(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setUsername(dto.getUsername());
        String userPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(userPassword);
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
            user.setRoles(newRoles);
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    @Override
    public User updateUser(UserDto dto) {
        User user = findById(dto.getId());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setUsername(dto.getUsername());
        if(dto.getPassword() != null && !dto.getPassword().isBlank()){
        String newPass = passwordEncoder.encode(dto.getPassword());
        user.setPassword(newPass);
        }
        Set<Role> newRoles;
        if (dto.getRoleIds() != null) {
            newRoles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
        } else {
            newRoles = new HashSet<>();
        }
        user.setRoles(newRoles);
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
