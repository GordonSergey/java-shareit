package ru.practicum.shareit.dtoTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("JSON тесты для Item DTO")
public class ItemDtoJsonTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Сериализация ItemDto в JSON")
    void itemDto_SerializationTest() throws Exception {
        // given
        var lastBooking = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 1, 1, 12, 0))
                .end(LocalDateTime.of(2023, 1, 2, 12, 0))
                .build();

        var comment = CommentDto.builder()
                .id(1L)
                .text("Отличная вещь!")
                .authorName("Иван Иванов")
                .created(LocalDateTime.of(2023, 1, 3, 12, 0))
                .build();

        var itemDto = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(null)
                .comments(List.of(comment))
                .requestId(10L)
                .build();

        // when
        var json = objectMapper.writeValueAsString(itemDto);

        // then
        assertThat(json).isEqualTo("{" +
                "\"id\":1," +
                "\"name\":\"Дрель\"," +
                "\"description\":\"Аккумуляторная дрель\"," +
                "\"available\":true," +
                "\"lastBooking\":{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\",\"itemId\":0,\"bookerId\":0,\"status\":null}," +
                "\"nextBooking\":null," +
                "\"comments\":[{\"id\":1,\"text\":\"Отличная вещь!\",\"authorName\":\"Иван Иванов\",\"created\":\"2023-01-03T12:00:00\"}]," +
                "\"requestId\":10" +
                "}");
    }

    @Test
    @DisplayName("Десериализация JSON в ItemDto")
    void itemDto_DeserializationTest() throws Exception {
        // given
        var json = "{" +
                "\"id\":1," +
                "\"name\":\"Дрель\"," +
                "\"description\":\"Аккумуляторная дрель\"," +
                "\"available\":true," +
                "\"lastBooking\":{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"}," +
                "\"comments\":[{\"id\":1,\"text\":\"Отличная вещь!\",\"authorName\":\"Иван Иванов\",\"created\":\"2023-01-03T12:00:00\"}]," +
                "\"requestId\":10" +
                "}";

        // when
        var itemDto = objectMapper.readValue(json, ItemDto.class);

        // then
        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Дрель");
        assertThat(itemDto.getDescription()).isEqualTo("Аккумуляторная дрель");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getLastBooking().getId()).isEqualTo(1L);
        assertThat(itemDto.getComments()).hasSize(1);
        assertThat(itemDto.getRequestId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Сериализация ItemRequestDto в JSON")
    void itemRequestDto_SerializationTest() throws Exception {
        // given
        var itemRequestDto = ItemRequestDto.builder()
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .build();

        // when
        var json = objectMapper.writeValueAsString(itemRequestDto);

        // then
        assertThat(json).isEqualTo("{" +
                "\"name\":\"Дрель\"," +
                "\"description\":\"Аккумуляторная дрель\"," +
                "\"available\":true" +
                "}");
    }

    @Test
    @DisplayName("Десериализация JSON в ItemRequestDto")
    void itemRequestDto_DeserializationTest() throws Exception {
        // given
        var json = "{" +
                "\"name\":\"Дрель\"," +
                "\"description\":\"Аккумуляторная дрель\"," +
                "\"available\":true" +
                "}";

        // when
        var itemRequestDto = objectMapper.readValue(json, ItemRequestDto.class);

        // then
        assertThat(itemRequestDto.getName()).isEqualTo("Дрель");
        assertThat(itemRequestDto.getDescription()).isEqualTo("Аккумуляторная дрель");
        assertThat(itemRequestDto.getAvailable()).isTrue();
    }

    @Test
    @DisplayName("Сериализация ItemUpdateRequestDto в JSON")
    void itemUpdateRequestDto_SerializationTest() throws Exception {
        // given
        var updateRequest = ItemUpdateRequestDto.builder()
                .name("Дрель+")
                .description("Аккумуляторная дрель с перфоратором")
                .available(false)
                .build();

        // when
        var json = objectMapper.writeValueAsString(updateRequest);

        // then
        assertThat(json).isEqualTo("{" +
                "\"name\":\"Дрель+\"," +
                "\"description\":\"Аккумуляторная дрель с перфоратором\"," +
                "\"available\":false" +
                "}");
    }

    @Test
    @DisplayName("Десериализация частичного ItemUpdateRequestDto")
    void itemUpdateRequestDto_PartialDeserializationTest() throws Exception {
        // given
        var json = "{\"name\":\"Дрель+\"}";

        // when
        var updateRequest = objectMapper.readValue(json, ItemUpdateRequestDto.class);

        // then
        assertThat(updateRequest.getName()).isEqualTo("Дрель+");
        assertThat(updateRequest.getDescription()).isNull();
        assertThat(updateRequest.getAvailable()).isNull();
    }

    @Test
    @DisplayName("Десериализация ItemDto с null значениями")
    void itemDto_DeserializationWithNullsTest() throws Exception {
        // given
        var json = "{\"name\":\"Дрель\",\"available\":true}";

        // when
        var itemDto = objectMapper.readValue(json, ItemDto.class);

        // then
        assertThat(itemDto.getId()).isNull();
        assertThat(itemDto.getName()).isEqualTo("Дрель");
        assertThat(itemDto.getDescription()).isNull();
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getLastBooking()).isNull();
        assertThat(itemDto.getComments()).isNull();
    }
}