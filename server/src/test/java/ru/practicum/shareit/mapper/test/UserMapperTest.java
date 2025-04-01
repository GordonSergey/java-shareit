package ru.practicum.shareit.mapper.test;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void mapToUserDto_whenUserIsValid_thenReturnUserDto() {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");

        UserDto dto = UserMapper.mapToUserDto(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void mapToUserDtoList_whenUsersProvided_thenReturnDtoList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");
        user1.setEmail("alice@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");

        List<User> users = List.of(user1, user2);

        List<UserDto> dtos = UserMapper.mapToUserDto(users);

        assertEquals(2, dtos.size());

        assertEquals(user1.getId(), dtos.get(0).getId());
        assertEquals(user2.getId(), dtos.get(1).getId());
    }

    @Test
    void mapToNewUser_whenUserDtoProvided_thenReturnUser() {
        UserDto dto = UserDto.builder()
                .name("Charlie")
                .email("charlie@example.com")
                .build();

        User user = UserMapper.mapToNewUser(dto);

        assertNotNull(user);
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
    }
}