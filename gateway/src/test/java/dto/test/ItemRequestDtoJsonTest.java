package dto.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JSON тесты для ItemRequest DTO")
public class ItemRequestDtoJsonTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Десериализация JSON в ItemRequestDto")
    void itemRequestDto_DeserializationTest() throws Exception {
        var json = "{" +
                "\"id\":1," +
                "\"description\":\"Нужна дрель для ремонта\"," +
                "\"created\":\"2023-01-01T12:00:00\"" +
                "}";

        var requestDto = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(requestDto.getId()).isEqualTo(1L);
        assertThat(requestDto.getDescription()).isEqualTo("Нужна дрель для ремонта");
        assertThat(requestDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Десериализация ItemRequestDto с null значениями")
    void itemRequestDto_DeserializationWithNullsTest() throws Exception {
        var json = "{\"description\":\"Нужна дрель для ремонта\"}";

        var requestDto = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(requestDto.getId()).isNull();
        assertThat(requestDto.getDescription()).isEqualTo("Нужна дрель для ремонта");
        assertThat(requestDto.getCreated()).isNull();
    }

    @Test
    @DisplayName("Десериализация JSON в ItemRequestWithResponsesDto")
    void itemRequestWithResponsesDto_DeserializationTest() throws Exception {
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

        var requestWithItems = objectMapper.readValue(json, ItemRequestWithResponsesDto.class);

        assertThat(requestWithItems.getId()).isEqualTo(1L);
        assertThat(requestWithItems.getDescription()).isEqualTo("Нужна дрель для ремонта");
        assertThat(requestWithItems.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(requestWithItems.getItems()).hasSize(1);
        assertThat(requestWithItems.getItems().get(0))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Дрель")
                .hasFieldOrPropertyWithValue("available", true);
    }

    @Test
    @DisplayName("Десериализация ItemRequestWithResponsesDto без items")
    void itemRequestWithResponsesDto_DeserializationWithoutItemsTest() throws Exception {
        var json = "{" +
                "\"id\":1," +
                "\"description\":\"Нужна дрель для ремонта\"," +
                "\"created\":\"2023-01-01T12:00:00\"" +
                "}";

        var requestWithItems = objectMapper.readValue(json, ItemRequestWithResponsesDto.class);

        assertThat(requestWithItems.getId()).isEqualTo(1L);
        assertThat(requestWithItems.getDescription()).isEqualTo("Нужна дрель для ремонта");
        assertThat(requestWithItems.getItems()).isNull();
    }
}