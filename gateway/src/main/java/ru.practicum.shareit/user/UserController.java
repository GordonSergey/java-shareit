package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    // POST /users
    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("GATEWAY: POST /users: {}", userDto);
        return userClient.createUser(userDto);
    }

    // PATCH /users/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable @Positive long id,
                                             @RequestBody UserDto userDto) {
        log.info("GATEWAY: PATCH /users/{}: {}", id, userDto);
        return userClient.updateUser(id, userDto);
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable @Positive long id) {
        log.info("GATEWAY: GET /users/{}", id);
        return userClient.getUserById(id);
    }

    // GET /users
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("GATEWAY: GET /users");
        return userClient.getAllUsers();
    }

    // DELETE /users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Positive long id) {
        log.info("GATEWAY: DELETE /users/{}", id);
        return userClient.deleteUser(id);
    }
}