package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User.UserDto addUser(@RequestBody User.UserDto user) {
        return userService.addUser(user);
    }

    @GetMapping
    public List<User.UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User.UserDto getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public User.UserDto updateUser(@RequestBody User.UserDto user, @PathVariable Integer userId) {
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/{userId}")
    public User.UserDto deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }
}