package io.slava.usermanager.dto;

import jakarta.validation.constraints.*;

import java.util.Set;

public class UserDto {
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 1, max = 50, message = "Имя должно быть от 1 до 50 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Имя должно содержать только буквы")
    private String name;

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(min = 1, max = 50, message = "Фамилия должна быть от 1 до 50 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Фамилия должно содержать только буквы")
    private String lastName;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 1, message = "Возраст не может быть отрицательным или 0")
    @Max(value = 150, message = "Возраст не может превышать 150")
    private Integer age;

    @NotBlank
    @Size(min = 1, max = 50, message = "Логин должен состоять из 1 до 50 символов")
    private String username;
    @Pattern(regexp = "^$|^.{4,50}$", message = "Пароль должен содержать более 4 симоволов")
    private String password;

    private Set<Long> roleIds;

    public UserDto() {
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
