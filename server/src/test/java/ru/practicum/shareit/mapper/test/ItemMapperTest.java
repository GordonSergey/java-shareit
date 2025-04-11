package ru.practicum.shareit.mapper.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    @Test
    @DisplayName("mapToItemDto: should return empty list when input iterable is empty")
    void mapToItemDto_iterableEmpty() {
        List<ItemDto> dtos = ItemMapper.mapToItemDto(Collections.emptyList());
        assertThat(dtos).isEmpty();
    }

    @Test
    @DisplayName("mapToItemDto: should map iterable of items")
    void mapToItemDto_iterableOfItems() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Отвертка");
        item.setDescription("Плоская отвертка");
        item.setAvailable(false);

        List<ItemDto> dtos = ItemMapper.mapToItemDto(List.of(item));

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getName()).isEqualTo("Отвертка");
    }

    @Test
    @DisplayName("mapToNewItem: should convert DTO to new Item entity")
    void mapToNewItem_fromDto() {
        ItemDto dto = ItemDto.builder()
                .name("Лампа")
                .description("Настольная лампа")
                .available(true)
                .requestId(5L)
                .build();

        Item item = ItemMapper.mapToNewItem(dto);

        assertThat(item).isNotNull();
        assertThat(item.getName()).isEqualTo("Лампа");
        assertThat(item.getDescription()).isEqualTo("Настольная лампа");
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getOwner()).isEqualTo(0);
        assertThat(item.getRequest().getId()).isEqualTo(5L);
    }
}