package ru.practicum.shareit.dtoTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("JSON тесты для ItemRequest DTO")
public class ItemRequestDtoJsonTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Сериализация ItemRequestDto в JSON")
    void itemRequestDto_SerializationTest() throws Exception {
        // given
        var requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Нужна дрель для ремонта")
                .created(LocalDateTime.of(2023, 1, 1, 12, 0))
                .build();

        // when
        var json = objectMapper.writeValueAsString(requestDto);

        // then
        assertThat(json).isEqualTo("{" +
                "\"id\":1," +
                "\"description\":\"Нужна дрель для ремонта\"," +
                "\"created\":\"2023-01-01T12:00:00\"" +
                "}");
    }

    @Test
    @DisplayName("Десериализация JSON в ItemRequestDto")
    void itemRequestDto_DeserializationTest() throws Exception {
        // given
        var json = "{" +
                "\"id\":1," +
                "\"description\":\"Нужна дрель для ремонта\"," +
                "\"created\":\"2023-01-01T12:00:00\"" +
                "}";

        // when
        var requestDto = objectMapper.readValue(json, ItemRequestDto.class);

        // then
        assertThat(requestDto.getId()).isEqualTo(1L);
        assertThat(requestDto.getDescription()).isEqualTo("Нужна дрель для ремонта");
        assertThat(requestDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Десериализация ItemRequestDto с null значениями")
    void itemRequestDto_DeserializationWithNullsTest() throws Exception {
        // given
        var json = "{\"description\":\"Нужна дрель для ремонта\"}";

        // when
        var requestDto = objectMapper.readValue(json, ItemRequestDto.class);

        // then
        assertThat(requestDto.getId()).isNull();
        assertThat(requestDto.getDescription()).isEqualTo("Нужна дрель для ремонта");
        assertThat(requestDto.getCreated()).isNull();
    }

    @Test
    @DisplayName("Сериализация ItemRequestWithResponsesDto в JSON")
    void itemRequestWithResponsesDto_SerializationTest() throws Exception {
        // given
        var item = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .build();

        var requestWithItems = ItemRequestWithResponsesDto.builder()
                .id(1L)
                .description("Нужна дрель для ремонта")
                .created(LocalDateTime.of(2023, 1, 1, 12, 0))
                .items(List.of(item))
                .build();

        // when
        var json = objectMapper.writeValueAsString(requestWithItems);

        // then
        assertThat(json).isEqualTo("{" +
                "\"id\":1," +
                "\"description\":\"Нужна дрель для ремонта\"," +
                "\"created\":\"2023-01-01T12:00:00\"," +
                "\"items\":[{" +
                "\"id\":1," +
                "\"name\":\"Дрель\"," +
                "\"description\":\"Аккумуляторная дрель\"," +
                "\"available\":true," +
                "\"lastBooking\":null," +
                "\"nextBooking\":null," +
                "\"comments\":null," +
                "\"requestId\":null" +
                "}]" +
                "}");
    }

    @Test
    @DisplayName("Десериализация JSON в ItemRequestWithResponsesDto")
    void itemRequestWithResponsesDto_DeserializationTest() throws Exception {
        // given
        var json = "{" +
                "\"id\":1," +
                "\"description\":\"Нужна дрель для ремонта\"," +
                "\"created\":\"2023-01-01T12:00:00\"," +
                "\"items\":[{" +
                "\"id\":1," +
                "\"name\":\"Дрель\"," +
                "\"description\":\"Аккумуляторная дрель\"," +
                "\"available\":true" +
                "}]" +
                "}";

        // when
        var requestWithItems = objectMapper.readValue(json, ItemRequestWithResponsesDto.class);

        // then
        assertThat(requestWithItems.getId()).isEqualTo(1L);
        assertThat(requestWithItems.getDescription()).isEqualTo("Нужна дрель для ремонта");
        assertThat(requestWithItems.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(requestWithItems.getItems()).hasSize(1);
        assertThat(requestWithItems.getItems().get(0).getName()).isEqualTo("Дрель");
    }

    @Test
    @DisplayName("Десериализация ItemRequestWithResponsesDto без items")
    void itemRequestWithResponsesDto_DeserializationWithoutItemsTest() throws Exception {
        // given
        var json = "{" +
                "\"id\":1," +
                "\"description\":\"Нужна дрель для ремонта\"," +
                "\"created\":\"2023-01-01T12:00:00\"" +
                "}";

        // when
        var requestWithItems = objectMapper.readValue(json, ItemRequestWithResponsesDto.class);

        // then
        assertThat(requestWithItems.getId()).isEqualTo(1L);
        assertThat(requestWithItems.getItems()).isNull();
    }

    @Test
    @DisplayName("Проверка обработки неизвестных полей в ItemRequestDto")
    void itemRequestDto_UnknownFieldsTest() throws Exception {
        // given
        var json = "{" +
                "\"id\":1," +
                "\"description\":\"Нужна дрель для ремонта\"," +
                "\"created\":\"2023-01-01T12:00:00\"," +
                "\"unknownField\":\"someValue\"" +
                "}";

        // when
        var requestDto = objectMapper.readValue(json, ItemRequestDto.class);

        // then
        assertThat(requestDto.getId()).isEqualTo(1L);
        assertThat(requestDto.getDescription()).isEqualTo("Нужна дрель для ремонта");
    }
}