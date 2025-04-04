package dto.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JSON тесты для User DTO")
public class UserDtoJsonTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Сериализация UserDto в JSON")
    void userDto_SerializationTest() throws Exception {
        var userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        var json = objectMapper.writeValueAsString(userDto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"John Doe\"");
        assertThat(json).contains("\"email\":\"john@example.com\"");
    }

    @Test
    @DisplayName("Десериализация JSON в UserDto")
    void userDto_DeserializationTest() throws Exception {
        var json = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}";

        var userDto = objectMapper.readValue(json, UserDto.class);

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("John Doe");
        assertThat(userDto.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Десериализация UserDto с null значениями")
    void userDto_DeserializationWithNullsTest() throws Exception {
        var json = "{\"name\":\"John Doe\"}";

        var userDto = objectMapper.readValue(json, UserDto.class);

        assertThat(userDto.getId()).isNull();
        assertThat(userDto.getName()).isEqualTo("John Doe");
        assertThat(userDto.getEmail()).isNull();
    }

    @Test
    @DisplayName("Сериализация UserCreateRequestDto в JSON")
    void userCreateRequestDto_SerializationTest() throws Exception {
        var createRequest = UserCreateRequestDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .build();

        var json = objectMapper.writeValueAsString(createRequest);

        assertThat(json).contains("\"name\":\"John Doe\"");
        assertThat(json).contains("\"email\":\"john@example.com\"");
    }

    @Test
    @DisplayName("Десериализация JSON в UserCreateRequestDto")
    void userCreateRequestDto_DeserializationTest() throws Exception {
        var json = "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}";

        var createRequest = objectMapper.readValue(json, UserCreateRequestDto.class);

        assertThat(createRequest.getName()).isEqualTo("John Doe");
        assertThat(createRequest.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Десериализация UserCreateRequestDto без email")
    void userCreateRequestDto_DeserializationWithoutEmailTest() throws Exception {
        var json = "{\"name\":\"John Doe\"}";

        var createRequest = objectMapper.readValue(json, UserCreateRequestDto.class);

        assertThat(createRequest.getName()).isEqualTo("John Doe");
        assertThat(createRequest.getEmail()).isNull();
    }

    @Test
    @DisplayName("Десериализация UserDto с пустым email")
    void userDto_EmptyEmailDeserializationTest() throws Exception {
        var json = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"\"}";

        var userDto = objectMapper.readValue(json, UserDto.class);

        assertThat(userDto.getEmail()).isEmpty();
    }
}