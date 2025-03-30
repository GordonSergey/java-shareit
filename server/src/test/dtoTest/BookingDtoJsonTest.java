package ru.practicum.shareit.dtoTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("JSON тесты для Booking DTO")
public class BookingDtoJsonTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Сериализация BookingDto в JSON")
    void bookingDto_SerializationTest() throws Exception {
        // given
        var bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 1, 1, 12, 0))
                .end(LocalDateTime.of(2023, 1, 2, 12, 0))
                .itemId(10L)
                .bookerId(5L)
                .status(BookingStatus.APPROVED)
                .build();

        // when
        var json = objectMapper.writeValueAsString(bookingDto);

        // then
        assertThat(json).isEqualTo("{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"," +
                "\"itemId\":10,\"bookerId\":5,\"status\":\"APPROVED\"}");
    }

    @Test
    @DisplayName("Десериализация JSON в BookingDto")
    void bookingDto_DeserializationTest() throws Exception {
        // given
        var json = "{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"," +
                "\"itemId\":10,\"bookerId\":5,\"status\":\"APPROVED\"}";

        // when
        var bookingDto = objectMapper.readValue(json, BookingDto.class);

        // then
        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStart()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(bookingDto.getEnd()).isEqualTo(LocalDateTime.of(2023, 1, 2, 12, 0));
        assertThat(bookingDto.getItemId()).isEqualTo(10L);
        assertThat(bookingDto.getBookerId()).isEqualTo(5L);
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("Сериализация BookingRequestDto в JSON")
    void bookingRequestDto_SerializationTest() throws Exception {
        // given
        var booker = UserDto.builder()
                .id(5L)
                .name("John Doe")
                .build();

        var bookingRequestDto = BookingRequestDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 1, 1, 12, 0))
                .end(LocalDateTime.of(2023, 1, 2, 12, 0))
                .itemId(10L)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();

        // when
        var json = objectMapper.writeValueAsString(bookingRequestDto);

        // then
        assertThat(json).isEqualTo("{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"," +
                "\"itemId\":10,\"booker\":{\"id\":5,\"name\":\"John Doe\",\"email\":null},\"status\":\"APPROVED\"}");
    }

    @Test
    @DisplayName("Десериализация JSON в BookingRequestDto")
    void bookingRequestDto_DeserializationTest() throws Exception {
        // given
        var json = "{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"," +
                "\"itemId\":10,\"booker\":{\"id\":5,\"name\":\"John Doe\"},\"status\":\"APPROVED\"}";

        // when
        var bookingRequestDto = objectMapper.readValue(json, BookingRequestDto.class);

        // then
        assertThat(bookingRequestDto.getId()).isEqualTo(1L);
        assertThat(bookingRequestDto.getStart()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(bookingRequestDto.getEnd()).isEqualTo(LocalDateTime.of(2023, 1, 2, 12, 0));
        assertThat(bookingRequestDto.getItemId()).isEqualTo(10L);
        assertThat(bookingRequestDto.getBooker().getId()).isEqualTo(5L);
        assertThat(bookingRequestDto.getBooker().getName()).isEqualTo("John Doe");
        assertThat(bookingRequestDto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("Десериализация JSON с null значениями в BookingDto")
    void bookingDto_DeserializationWithNullsTest() throws Exception {
        // given
        var json = "{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"}";

        // when
        var bookingDto = objectMapper.readValue(json, BookingDto.class);

        // then
        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStart()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(bookingDto.getEnd()).isEqualTo(LocalDateTime.of(2023, 1, 2, 12, 0));
        assertThat(bookingDto.getItemId()).isEqualTo(0L); // default for long
        assertThat(bookingDto.getBookerId()).isEqualTo(0L); // default for long
        assertThat(bookingDto.getStatus()).isNull();
    }

    @Test
    @DisplayName("Проверка обработки неизвестных полей в BookingDto")
    void bookingDto_UnknownFieldsTest() throws Exception {
        // given
        var json = "{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"," +
                "\"unknownField\":\"someValue\"}";

        // when
        var bookingDto = objectMapper.readValue(json, BookingDto.class);

        // then
        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStart()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(bookingDto.getEnd()).isEqualTo(LocalDateTime.of(2023, 1, 2, 12, 0));
    }
}