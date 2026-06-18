package io.slava.usermanager.service;


import io.slava.usermanager.dto.UserDto;
import io.slava.usermanager.model.User;

import java.util.List;

public interface UserService {
        User addUser(UserDto dto);
        List<User> getAllUsers();
        User findById(Long id);
        User updateUser(UserDto dto);
        void deleteById(Long id);
        User findByUsername(String username);
}
