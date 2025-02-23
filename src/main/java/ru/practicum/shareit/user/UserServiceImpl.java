package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMemoryRepository userMemoryRepository;

    @Override
    public User.UserDto addUser(User.UserDto userDtoRequest) {
        return userMemoryRepository.addUser(userDtoRequest);
    }

    @Override
    public List<User.UserDto> getUsers() {
        return userMemoryRepository.getUsers();
    }

    @Override
    public User.UserDto getUserById(Integer userId) {
        return userMemoryRepository.getUserById(userId);
    }

    @Override
    public User.UserDto updateUser(User.UserDto userDtoRequest, Integer userId) {
        return userMemoryRepository.updateUser(userDtoRequest, userId);
    }

    @Override
    public User.UserDto deleteUser(Integer userId) {
        return userMemoryRepository.deleteUser(userId);
    }
}