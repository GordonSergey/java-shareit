package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;

@Component
public class UserMapper {
    public User.UserDto mapToUserDto(User user) {
        if (user == null) {
            throw new NotFoundException("User must not be null");
        }

        User.UserDto userDto = new User.UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public User mapToUser(User.UserDto userDto) {
        User user = new User();

        user.setId(user.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }

    public static User updateUserFields(User user, User.UserDto request) {

        if (request.hasName()) {
            user.setName(request.getName());
        }

        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }

        return user;
    }
}