package dto.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JSON тесты для Item DTO")
public class ItemDtoJsonTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Десериализация JSON в ItemDto")
    void itemDto_DeserializationTest() throws Exception {
        var json = "{" +
                "\"id\":1," +
                "\"name\":\"Дрель\"," +
                "\"description\":\"Аккумуляторная дрель\"," +
                "\"available\":true," +
                "\"lastBooking\":{\"id\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"}," +
                "\"comments\":[{\"id\":1,\"text\":\"Отличная вещь!\",\"authorName\":\"Иван Иванов\",\"created\":\"2023-01-03T12:00:00\"}]," +
                "\"requestId\":10" +
                "}";

        var itemDto = objectMapper.readValue(json, ItemDto.class);

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Дрель");
        assertThat(itemDto.getDescription()).isEqualTo("Аккумуляторная дрель");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getLastBooking())
                .isNotNull()
                .extracting(BookingDto::getId)
                .isEqualTo(1L);
        assertThat(itemDto.getComments())
                .hasSize(1)
                .first()
                .extracting(CommentDto::getText)
                .isEqualTo("Отличная вещь!");
        assertThat(itemDto.getRequestId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Сериализация ItemRequestDto в JSON")
    void itemRequestDto_SerializationTest() throws Exception {
        var itemRequestDto = ItemRequestDto.builder()
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .build();

        var json = objectMapper.writeValueAsString(itemRequestDto);

        assertThat(json).contains("\"name\":\"Дрель\"");
        assertThat(json).contains("\"description\":\"Аккумуляторная дрель\"");
        assertThat(json).contains("\"available\":true");
    }

    @Test
    @DisplayName("Десериализация JSON в ItemRequestDto")
    void itemRequestDto_DeserializationTest() throws Exception {
        var json = "{" +
                "\"name\":\"Дрель\"," +
                "\"description\":\"Аккумуляторная дрель\"," +
                "\"available\":true" +
                "}";

        var itemRequestDto = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(itemRequestDto)
                .extracting(
                        ItemRequestDto::getName,
                        ItemRequestDto::getDescription,
                        ItemRequestDto::getAvailable
                )
                .containsExactly(
                        "Дрель",
                        "Аккумуляторная дрель",
                        true
                );
    }

    @Test
    @DisplayName("Сериализация ItemUpdateRequestDto в JSON")
    void itemUpdateRequestDto_SerializationTest() throws Exception {
        var updateRequest = ItemUpdateRequestDto.builder()
                .name("Дрель+")
                .description("Аккумуляторная дрель с перфоратором")
                .available(false)
                .build();

        var json = objectMapper.writeValueAsString(updateRequest);

        assertThat(json).contains("\"name\":\"Дрель+\"");
        assertThat(json).contains("\"description\":\"Аккумуляторная дрель с перфоратором\"");
        assertThat(json).contains("\"available\":false");
    }

    @Test
    @DisplayName("Десериализация частичного ItemUpdateRequestDto")
    void itemUpdateRequestDto_PartialDeserializationTest() throws Exception {
        var json = "{\"name\":\"Дрель+\"}";

        var updateRequest = objectMapper.readValue(json, ItemUpdateRequestDto.class);

        assertThat(updateRequest)
                .extracting(
                        ItemUpdateRequestDto::getName,
                        ItemUpdateRequestDto::getDescription,
                        ItemUpdateRequestDto::getAvailable
                )
                .containsExactly(
                        "Дрель+",
                        null,
                        null
                );
    }

    @Test
    @DisplayName("Десериализация ItemDto с null значениями")
    void itemDto_DeserializationWithNullsTest() throws Exception {
        var json = "{\"name\":\"Дрель\",\"available\":true}";

        var itemDto = objectMapper.readValue(json, ItemDto.class);

        assertThat(itemDto)
                .extracting(
                        ItemDto::getId,
                        ItemDto::getName,
                        ItemDto::getDescription,
                        ItemDto::getAvailable,
                        ItemDto::getLastBooking,
                        ItemDto::getComments
                )
                .containsExactly(
                        null,
                        "Дрель",
                        null,
                        true,
                        null,
                        null
                );
    }
}