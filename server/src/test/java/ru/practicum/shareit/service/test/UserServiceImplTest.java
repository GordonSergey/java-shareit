package ru.practicum.shareit.service.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_returnsListOfUserDto() {
        List<User> users = List.of(new User(1L, "User", "user@example.com"));
        when(repository.findAll()).thenReturn(users);

        List<UserDto> result = service.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("User", result.get(0).getName());
    }

    @Test
    void saveUser_validUser_returnsUserDto() {
        UserDto input = new UserDto(null, "User", "user@example.com");
        User saved = new User(1L, "User", "user@example.com");
        when(repository.save(any())).thenReturn(saved);

        UserDto result = service.saveUser(input);

        assertEquals("User", result.getName());
        verify(repository).save(any());
    }

    @Test
    void saveUser_blankName_throwsValidationException() {
        UserDto input = new UserDto(null, "", "user@example.com");

        assertThrows(ValidationException.class, () -> service.saveUser(input));
    }

    @Test
    void saveUser_blankEmail_throwsValidationException() {
        UserDto input = new UserDto(null, "User", "");

        assertThrows(ValidationException.class, () -> service.saveUser(input));
    }

    @Test
    void getUserNameById_validId_returnsName() {
        User user = new User(1L, "User", "user@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        String name = service.getUserNameById(1L);

        assertEquals("User", name);
    }

    @Test
    void getUserNameById_invalidId_throwsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getUserNameById(1L));
    }

    @Test
    void update_validData_returnsUpdatedUser() {
        User existing = new User(1L, "OldName", "old@example.com");
        UserDto updateDto = new UserDto(1L, "NewName", "new@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail("new@example.com")).thenReturn(false);

        UserDto result = service.update(1L, updateDto);

        assertEquals("NewName", result.getName());
        assertEquals("new@example.com", result.getEmail());
        verify(repository).updateUser(1L, "NewName", "new@example.com");
    }

    @Test
    void update_duplicateEmail_throwsConflictException() {
        User existing = new User(1L, "OldName", "old@example.com");
        UserDto updateDto = new UserDto(1L, "NewName", "duplicate@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail("duplicate@example.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.update(1L, updateDto));
    }

    @Test
    void deleteUserById_validId_executesDeletion() {
        service.deleteUserById(1L);
        verify(repository).deleteUserById(1L);
    }

    @Test
    void deleteUserById_invalidId_throwsValidationException() {
        assertThrows(ValidationException.class, () -> service.deleteUserById(0));
    }
}