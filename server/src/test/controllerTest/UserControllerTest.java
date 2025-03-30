package ru.practicum.shareit.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-тесты UserController")
public class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Создание пользователя - успешный сценарий")
    void saveUser_ValidRequest_ReturnsCreated() throws Exception {
        // given
        var userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");

        var savedUser = new UserDto();
        savedUser.setId(1L);
        savedUser.setName(userDto.getName());
        savedUser.setEmail(userDto.getEmail());

        doReturn(savedUser)
                .when(userService)
                .saveUser(any(UserDto.class));

        // when
        var requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto));

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.name").value("John Doe"),
                        jsonPath("$.email").value("john@example.com")
                );

        verify(userService).saveUser(any(UserDto.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Получение всех пользователей - успешный сценарий")
    void getAllUsers_ValidRequest_ReturnsOk() throws Exception {
        // given
        var user1 = new UserDto();
        user1.setId(1L);
        user1.setName("User 1");

        var user2 = new UserDto();
        user2.setId(2L);
        user2.setName("User 2");

        var users = List.of(user1, user2);

        doReturn(users)
                .when(userService)
                .getAllUsers();

        // when
        var requestBuilder = MockMvcRequestBuilders.get("/users");

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].name").value("User 1"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].name").value("User 2")
                );

        verify(userService).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Получение пользователя по ID - успешный сценарий")
    void getUserById_ValidRequest_ReturnsOk() throws Exception {
        // given
        var userId = 1L;

        var user = new UserDto();
        user.setId(userId);
        user.setName("Test User");

        doReturn(user)
                .when(userService)
                .getUserById(userId);

        // when
        var requestBuilder = MockMvcRequestBuilders.get("/users/{userId}", userId);

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(userId),
                        jsonPath("$.name").value("Test User")
                );

        verify(userService).getUserById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Обновление пользователя - успешный сценарий")
    void update_ValidRequest_ReturnsOk() throws Exception {
        // given
        var userId = 1L;

        var updateDto = new UserDto();
        updateDto.setName("Updated Name");

        var updatedUser = new UserDto();
        updatedUser.setId(userId);
        updatedUser.setName("Updated Name");

        doReturn(updatedUser)
                .when(userService)
                .update(eq(userId), any(UserDto.class));

        // when
        var requestBuilder = MockMvcRequestBuilders.patch("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto));

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(userId),
                        jsonPath("$.name").value("Updated Name")
                );

        verify(userService).update(eq(userId), any(UserDto.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Удаление пользователя - успешный сценарий")
    void deleteUserById_ValidRequest_ReturnsNoContent() throws Exception {
        // given
        var userId = 1L;

        doNothing()
                .when(userService)
                .deleteUserById(userId);

        // when
        var requestBuilder = MockMvcRequestBuilders.delete("/users/{userId}", userId);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(userId);
        verifyNoMoreInteractions(userService);
    }
}