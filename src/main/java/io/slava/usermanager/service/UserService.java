package io.slava.usermanager.service;


import io.slava.usermanager.dto.UserEditDto;
import io.slava.usermanager.model.User;

import java.util.List;

public interface UserService {
        void addUser(User user, List<Long> roleIds);
        List<User> getAllUsers();
        User findById(Long id);
        void updateUser(UserEditDto dto);
        void deleteById(Long id);
        User findByUsername(String username);
}
