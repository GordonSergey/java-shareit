package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.service.IdGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Repository
public class UserMemoryRepository {
    private final UserMapper userMapper;
    private final IdGenerator idGenerator;
    Map<Integer, User> usersMap = new HashMap<>();

    public User.UserDto addUser(User.UserDto userDtoRequest) {
        String email = userDtoRequest.getEmail();

        if (email == null || email.isEmpty()) {
            throw new ValidationException("A user must have an email.");
        }

        emailValidator(email);

        User user = userMapper.mapToUser(userDtoRequest);
        user.setId(idGenerator.getNextId(usersMap));

        usersMap.put(user.getId(), user);
        return userMapper.mapToUserDto(user);
    }

    public List<User.UserDto> getUsers() {
        return usersMap.values().stream()
                .map(userMapper::mapToUserDto)
                .toList();
    }

    public User.UserDto getUserById(Integer userId) {
        return userMapper.mapToUserDto(usersMap.get(userId));
    }

    public User.UserDto updateUser(User.UserDto userDtoRequest, Integer userId) {
        User user = Optional.ofNullable(usersMap.get(userId))
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found."));

        if (userDtoRequest.getEmail() != null) {
            emailValidator(userDtoRequest.getEmail());
        }

        user = UserMapper.updateUserFields(user, userDtoRequest);
        usersMap.put(userId, user);

        return userMapper.mapToUserDto(user);
    }

    public User.UserDto deleteUser(Integer userId) {
        User.UserDto user = userMapper.mapToUserDto(usersMap.get(userId));
        usersMap.remove(userId);
        return user;
    }

    public void emailValidator(String email) {
        String emailRegex = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}";

        if (!Pattern.matches(emailRegex, email)) {
            throw new ValidationException("Email is in an invalid format.");
        }

        if (usersMap.values().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new DuplicatedDataException("User email already exists.");
        }
    }
}