package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.service.IdGenerator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserMemoryRepository;

import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ItemMemoryRepository extends IdGenerator {
    private final ItemMapper itemMapper;
    private final UserMemoryRepository userMemoryRepository;
    Map<Integer, Item> itemsMap = new HashMap<>();

    public ItemDto addItem(ItemDto itemDtoRequest, Integer userId) {
        if (userMemoryRepository.getUserById(userId) == null) {
            throw new NotFoundException("User with the given ID was not found.");
        }

        Item item = itemMapper.mapToItem(itemDtoRequest);
        item.setOwner(userMemoryRepository.getUserById(userId));
        item.setId(getNextId(itemsMap));
        itemsMap.put(item.getId(), item);

        return itemMapper.mapToItemDto(item);
    }

    public ItemDto updateItem(Integer itemId, ItemUpdateDto itemDtoRequest, Integer userId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item with the given ID was not found.");
        }

        Integer itemsOwnerId = itemsMap.get(itemId).getOwner().getId();
        if (!Objects.equals(itemsOwnerId, userId)) {
            throw new AccessDeniedException("Only the owner can modify the item.");
        }

        Item updatedItem = new Item();
        updatedItem.setName(itemDtoRequest.getName());
        updatedItem.setDescription(itemDtoRequest.getDescription());
        updatedItem.setAvailable(itemDtoRequest.getAvailable());

        itemsMap.put(itemId, updatedItem);

        return itemMapper.mapToItemDto(updatedItem);
    }

    public ItemDto getItem(Integer itemId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item with the given ID was not found.");
        }

        return itemMapper.mapToItemDto(itemsMap.get(itemId));
    }

    public List<ItemDto> getOwnerItems(Integer ownerId) {
        Collection<Item> allItems = itemsMap.values();
        List<ItemDto> ownerItemsList = new ArrayList<>();

        allItems.forEach(item -> {
            User owner = item.getOwner();
            if (owner != null && Objects.equals(owner.getId(), ownerId)) {
                ownerItemsList.add(itemMapper.mapToItemDto(item));
            }
        });

        if (ownerItemsList.isEmpty()) {
            throw new NotFoundException("User's items were not found.");
        }

        return ownerItemsList;
    }

    public List<ItemDto> itemSearch(String text) {
        List<ItemDto> searchItems = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            Collection<Item> allItems = itemsMap.values();

            allItems.forEach(item -> {
                if (item.getName() != null && Boolean.TRUE.equals(item.getAvailable()) &&
                        item.getName().toLowerCase().contains(text.toLowerCase())) {
                    searchItems.add(itemMapper.mapToItemDto(item));
                }

                if (item.getDescription() != null && Boolean.TRUE.equals(item.getAvailable()) &&
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    searchItems.add(itemMapper.mapToItemDto(item));
                }
            });
        }
        return searchItems;
    }
}