package dto.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JSON тесты для Booking DTO")
public class BookingDtoJsonTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Десериализация JSON в BookingDto")
    void bookingDto_DeserializationTest() throws Exception {
        var json = "{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"," +
                "\"itemId\":10,\"bookerId\":5,\"status\":\"APPROVED\"}";

        var bookingDto = objectMapper.readValue(json, BookingDto.class);

        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStart()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(bookingDto.getEnd()).isEqualTo(LocalDateTime.of(2023, 1, 2, 12, 0));
        assertThat(bookingDto.getItemId()).isEqualTo(10L);
        assertThat(bookingDto.getBookerId()).isEqualTo(5L);
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("Десериализация JSON в BookingRequestDto")
    void bookingRequestDto_DeserializationTest() throws Exception {
        var json = "{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"," +
                "\"itemId\":10,\"booker\":{\"id\":5,\"name\":\"John Doe\",\"email\":null},\"status\":\"APPROVED\"}";

        var bookingRequestDto = objectMapper.readValue(json, BookingRequestDto.class);

        assertThat(bookingRequestDto.getId()).isEqualTo(1L);
        assertThat(bookingRequestDto.getStart()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(bookingRequestDto.getEnd()).isEqualTo(LocalDateTime.of(2023, 1, 2, 12, 0));
        assertThat(bookingRequestDto.getItemId()).isEqualTo(10L);
        assertThat(bookingRequestDto.getBooker().getId()).isEqualTo(5L);
        assertThat(bookingRequestDto.getBooker().getName()).isEqualTo("John Doe");
        assertThat(bookingRequestDto.getBooker().getEmail()).isNull();
        assertThat(bookingRequestDto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("Десериализация JSON с null значениями в BookingDto")
    void bookingDto_DeserializationWithNullsTest() throws Exception {
        var json = "{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"}";

        var bookingDto = objectMapper.readValue(json, BookingDto.class);

        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStart()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(bookingDto.getEnd()).isEqualTo(LocalDateTime.of(2023, 1, 2, 12, 0));
        assertThat(bookingDto.getItemId()).isEqualTo(0L);
        assertThat(bookingDto.getBookerId()).isEqualTo(0L);
        assertThat(bookingDto.getStatus()).isNull();
    }
}