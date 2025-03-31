package dtoTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JSON тесты для Comment DTO")
public class CommentDtoJsonTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Десериализация JSON в CommentDto")
    void commentDto_DeserializationTest() throws Exception {
        var json = "{\"id\":1,\"text\":\"Отличная вещь!\"," +
                "\"authorName\":\"Иван Иванов\",\"created\":\"2023-01-01T12:00:00\"}";

        var commentDto = objectMapper.readValue(json, CommentDto.class);

        assertThat(commentDto.getId()).isEqualTo(1L);
        assertThat(commentDto.getText()).isEqualTo("Отличная вещь!");
        assertThat(commentDto.getAuthorName()).isEqualTo("Иван Иванов");
        assertThat(commentDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Десериализация CommentDto с null значениями")
    void commentDto_DeserializationWithNullsTest() throws Exception {
        var json = "{\"text\":\"Отличная вещь!\",\"created\":\"2023-01-01T12:00:00\"}";

        var commentDto = objectMapper.readValue(json, CommentDto.class);

        assertThat(commentDto.getId()).isNull();
        assertThat(commentDto.getText()).isEqualTo("Отличная вещь!");
        assertThat(commentDto.getAuthorName()).isNull();
        assertThat(commentDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Сериализация CommentItemRequestDto в JSON")
    void commentItemRequestDto_SerializationTest() throws Exception {
        var commentRequest = new CommentItemRequestDto("Нужна срочно!");

        var json = objectMapper.writeValueAsString(commentRequest);

        assertThat(json).isEqualTo("{\"text\":\"Нужна срочно!\"}");
    }

    @Test
    @DisplayName("Десериализация JSON в CommentItemRequestDto")
    void commentItemRequestDto_DeserializationTest() throws Exception {
        var json = "{\"text\":\"Нужна срочно!\"}";

        var commentRequest = objectMapper.readValue(json, CommentItemRequestDto.class);

        assertThat(commentRequest.getText()).isEqualTo("Нужна срочно!");
    }

    @Test
    @DisplayName("Десериализация пустого CommentItemRequestDto")
    void commentItemRequestDto_EmptyDeserializationTest() throws Exception {
        var json = "{}";

        var commentRequest = objectMapper.readValue(json, CommentItemRequestDto.class);

        assertThat(commentRequest.getText()).isNull();
    }
}