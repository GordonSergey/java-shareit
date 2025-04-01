package ru.practicum.shareit.mapper.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    @Test
    @DisplayName("toDto() should correctly map ItemRequest to ItemRequestDto")
    void toDto_ShouldMapCorrectly() {
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Нужен шуруповерт")
                .created(LocalDateTime.now())
                .build();

        ItemRequestDto result = ItemRequestMapper.toDto(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(request.getId());
        assertThat(result.getDescription()).isEqualTo(request.getDescription());
        assertThat(result.getCreated()).isEqualTo(request.getCreated());
    }

    @Test
    @DisplayName("toDto() should return null when input is null")
    void toDto_ShouldReturnNull_WhenInputIsNull() {
        assertThat(ItemRequestMapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("toDtoWithItems() should correctly map ItemRequest with items")
    void toDtoWithItems_ShouldMapCorrectly() {
        ItemRequest request = ItemRequest.builder()
                .id(2L)
                .description("Нужен ноутбук")
                .created(LocalDateTime.now())
                .build();

        ItemDto item = ItemDto.builder()
                .id(10L)
                .name("Ноутбук")
                .description("Игровой ноутбук")
                .available(true)
                .requestId(request.getId())
                .build();

        ItemRequestWithResponsesDto result = ItemRequestMapper.toDtoWithItems(request, List.of(item));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(request.getId());
        assertThat(result.getDescription()).isEqualTo(request.getDescription());
        assertThat(result.getCreated()).isEqualTo(request.getCreated());
        assertThat(result.getItems()).containsExactly(item);
    }

    @Test
    @DisplayName("toDtoWithItems() should return empty item list if null is passed")
    void toDtoWithItems_ShouldReturnEmptyList_WhenItemsAreNull() {
        ItemRequest request = ItemRequest.builder()
                .id(3L)
                .description("Нужен проектор")
                .created(LocalDateTime.now())
                .build();

        ItemRequestWithResponsesDto result = ItemRequestMapper.toDtoWithItems(request, null);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    @DisplayName("toDtoWithItems() should return null when input is null")
    void toDtoWithItems_ShouldReturnNull_WhenInputIsNull() {
        assertThat(ItemRequestMapper.toDtoWithItems(null, List.of())).isNull();
    }
}