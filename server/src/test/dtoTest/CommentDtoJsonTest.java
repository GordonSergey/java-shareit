package ru.practicum.shareit.dtoTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("JSON тесты для Comment DTO")
public class CommentDtoJsonTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Сериализация CommentDto в JSON")
    void commentDto_SerializationTest() throws Exception {
        // given
        var commentDto = CommentDto.builder()
                .id(1L)
                .text("Отличная вещь!")
                .authorName("Иван Иванов")
                .created(LocalDateTime.of(2023, 1, 1, 12, 0))
                .build();

        // when
        var json = objectMapper.writeValueAsString(commentDto);

        // then
        assertThat(json).isEqualTo("{\"id\":1,\"text\":\"Отличная вещь!\"," +
                "\"authorName\":\"Иван Иванов\",\"created\":\"2023-01-01T12:00:00\"}");
    }

    @Test
    @DisplayName("Десериализация JSON в CommentDto")
    void commentDto_DeserializationTest() throws Exception {
        // given
        var json = "{\"id\":1,\"text\":\"Отличная вещь!\"," +
                "\"authorName\":\"Иван Иванов\",\"created\":\"2023-01-01T12:00:00\"}";

        // when
        var commentDto = objectMapper.readValue(json, CommentDto.class);

        // then
        assertThat(commentDto.getId()).isEqualTo(1L);
        assertThat(commentDto.getText()).isEqualTo("Отличная вещь!");
        assertThat(commentDto.getAuthorName()).isEqualTo("Иван Иванов");
        assertThat(commentDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Десериализация CommentDto с null значениями")
    void commentDto_DeserializationWithNullsTest() throws Exception {
        // given
        var json = "{\"text\":\"Отличная вещь!\",\"created\":\"2023-01-01T12:00:00\"}";

        // when
        var commentDto = objectMapper.readValue(json, CommentDto.class);

        // then
        assertThat(commentDto.getId()).isNull();
        assertThat(commentDto.getText()).isEqualTo("Отличная вещь!");
        assertThat(commentDto.getAuthorName()).isNull();
        assertThat(commentDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Сериализация CommentItemRequestDto в JSON")
    void commentItemRequestDto_SerializationTest() throws Exception {
        // given
        var commentRequest = new CommentItemRequestDto("Нужна срочно!");

        // when
        var json = objectMapper.writeValueAsString(commentRequest);

        // then
        assertThat(json).isEqualTo("{\"text\":\"Нужна срочно!\"}");
    }

    @Test
    @DisplayName("Десериализация JSON в CommentItemRequestDto")
    void commentItemRequestDto_DeserializationTest() throws Exception {
        // given
        var json = "{\"text\":\"Нужна срочно!\"}";

        // when
        var commentRequest = objectMapper.readValue(json, CommentItemRequestDto.class);

        // then
        assertThat(commentRequest.getText()).isEqualTo("Нужна срочно!");
    }

    @Test
    @DisplayName("Десериализация пустого CommentItemRequestDto")
    void commentItemRequestDto_EmptyDeserializationTest() throws Exception {
        // given
        var json = "{}";

        // when
        var commentRequest = objectMapper.readValue(json, CommentItemRequestDto.class);

        // then
        assertThat(commentRequest.getText()).isNull();
    }
}