package ru.practicum.shareit.dtoTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("JSON тесты для User DTO")
public class UserDtoJsonTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Сериализация UserDto в JSON")
    void userDto_SerializationTest() throws Exception {
        // given
        var userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        // when
        var json = objectMapper.writeValueAsString(userDto);

        // then
        assertThat(json).isEqualTo("{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}");
    }

    @Test
    @DisplayName("Десериализация JSON в UserDto")
    void userDto_DeserializationTest() throws Exception {
        // given
        var json = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}";

        // when
        var userDto = objectMapper.readValue(json, UserDto.class);

        // then
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("John Doe");
        assertThat(userDto.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Десериализация UserDto с null значениями")
    void userDto_DeserializationWithNullsTest() throws Exception {
        // given
        var json = "{\"name\":\"John Doe\"}";

        // when
        var userDto = objectMapper.readValue(json, UserDto.class);

        // then
        assertThat(userDto.getId()).isNull();
        assertThat(userDto.getName()).isEqualTo("John Doe");
        assertThat(userDto.getEmail()).isNull();
    }

    @Test
    @DisplayName("Сериализация UserCreateRequestDto в JSON")
    void userCreateRequestDto_SerializationTest() throws Exception {
        // given
        var createRequest = UserCreateRequestDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .build();

        // when
        var json = objectMapper.writeValueAsString(createRequest);

        // then
        assertThat(json).isEqualTo("{\"name\":\"John Doe\",\"email\":\"john@example.com\"}");
    }

    @Test
    @DisplayName("Десериализация JSON в UserCreateRequestDto")
    void userCreateRequestDto_DeserializationTest() throws Exception {
        // given
        var json = "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}";

        // when
        var createRequest = objectMapper.readValue(json, UserCreateRequestDto.class);

        // then
        assertThat(createRequest.getName()).isEqualTo("John Doe");
        assertThat(createRequest.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Десериализация UserCreateRequestDto без email")
    void userCreateRequestDto_DeserializationWithoutEmailTest() throws Exception {
        // given
        var json = "{\"name\":\"John Doe\"}";

        // when
        var createRequest = objectMapper.readValue(json, UserCreateRequestDto.class);

        // then
        assertThat(createRequest.getName()).isEqualTo("John Doe");
        assertThat(createRequest.getEmail()).isNull();
    }

    @Test
    @DisplayName("Проверка обработки неизвестных полей в UserDto")
    void userDto_UnknownFieldsTest() throws Exception {
        // given
        var json = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\",\"unknown\":\"value\"}";

        // when
        var userDto = objectMapper.readValue(json, UserDto.class);

        // then
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("John Doe");
        assertThat(userDto.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Десериализация UserDto с пустым email")
    void userDto_EmptyEmailDeserializationTest() throws Exception {
        // given
        var json = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"\"}";

        // when
        var userDto = objectMapper.readValue(json, UserDto.class);

        // then
        assertThat(userDto.getEmail()).isEmpty();
    }
}