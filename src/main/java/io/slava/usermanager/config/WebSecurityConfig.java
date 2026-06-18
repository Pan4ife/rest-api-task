package io.slava.usermanager.config;

import io.slava.usermanager.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final SuccessUserHandler successUserHandler;
    private final UserDetailServiceImpl userDetailServiceImpl;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserDetailServiceImpl userDetailServiceImpl) {
        this.successUserHandler = successUserHandler;
        this.userDetailServiceImpl = userDetailServiceImpl;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                ).csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successUserHandler)
                        .permitAll()
                )
                .userDetailsService(userDetailServiceImpl)
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout"));
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}