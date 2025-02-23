package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    public ItemDto addItem(ItemDto itemDtoRequest, Integer userId);

    public ItemDto updateItem(Integer itemId, ItemDto itemDtoRequest, Integer userId);

    public ItemDto getItem(Integer itemId);

    public List<ItemDto> getOwnerItems(Integer ownerId);

    public List<ItemDto> itemSearch(String text);
}