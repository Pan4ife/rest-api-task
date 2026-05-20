package io.slava.usermanager.config;

import io.slava.usermanager.model.Role;
import io.slava.usermanager.model.User;
import io.slava.usermanager.repository.RoleRepository;
import io.slava.usermanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleRepository.save(adminRole);
        String adminRolePassword = passwordEncoder.encode("admin");

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleRepository.save(userRole);
        String userRolePassword = passwordEncoder.encode("user");

        User admin = new User("Slava", "Pan", 31);
        admin.setRoles(Set.of(adminRole));
        admin.setUsername("span");
        admin.setPassword(adminRolePassword);
        userRepository.save(admin);

        User user = new User("Andrey", "Pan", 23);
        user.setRoles(Set.of(userRole));
        user.setUsername("apan");
        user.setPassword(userRolePassword);
        userRepository.save(user);
    }
}
