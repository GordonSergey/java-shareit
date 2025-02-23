package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    User.UserDto addUser(User.UserDto userDtoRequest);

    List<User.UserDto> getUsers();

    User.UserDto updateUser(User.UserDto userDtoRequest, Integer userId);

    User.UserDto getUserById(Integer userId);

    User.UserDto deleteUser(Integer userId);
}