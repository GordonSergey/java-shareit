package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.service.IdGenerator;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserMemoryRepository extends IdGenerator {
    private final UserMapper userMapper;
    Map<Integer, User> usersMap = new HashMap<>();

    public User addUser(UserDto userDtoRequest) {
        if (userDtoRequest.getEmail() == null || userDtoRequest.getEmail().isEmpty()) {
            throw new ValidationException("A user must have an email.");
        }

        emailValidator(userDtoRequest.getEmail());

        User user = userMapper.mapToUser(userDtoRequest);
        user.setId(getNextId(usersMap));
        usersMap.put(user.getId(), user);

        return user;
    }

    public List<User> getUsers() {
        return new ArrayList<>(usersMap.values());
    }

    public User getUserById(Integer userId) {
        return usersMap.get(userId);
    }

    public User updateUser(UserDto userDtoRequest, Integer userId) {
        if (!usersMap.containsKey(userId)) {
            throw new RuntimeException("User with id " + userId + " not found.");
        }

        String email = userDtoRequest.getEmail();
        if (email != null) {
            emailValidator(userDtoRequest.getEmail());
        }

        User user = usersMap.get(userId);
        if (userDtoRequest.hasName()) {
            user.setName(userDtoRequest.getName());
        }
        if (userDtoRequest.hasEmail()) {
            user.setEmail(userDtoRequest.getEmail());
        }

        usersMap.put(userId, user);
        return user;
    }

    public User deleteUser(Integer userId) {
        return usersMap.remove(userId);
    }

    public void emailValidator(String email) {
        for (User user : usersMap.values()) {
            if (user.getEmail().equals(email)) {
                throw new DuplicatedDataException("User email already exists.");
            }
        }
    }
}