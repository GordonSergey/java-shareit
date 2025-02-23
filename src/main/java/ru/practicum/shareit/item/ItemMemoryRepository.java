package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.service.IdGenerator;
import ru.practicum.shareit.user.UserMemoryRepository;

import java.util.*;

@RequiredArgsConstructor
@Repository
public class ItemMemoryRepository {
    private final ItemMapper itemMapper;
    private final UserMemoryRepository userMemoryRepository;
    private final IdGenerator idGenerator;
    Map<Integer, Item> itemsMap = new HashMap<>();

    public ItemDto addItem(ItemDto itemDtoRequest, Integer userId) {

        if (itemDtoRequest.getAvailable() == null) {
            throw new ValidationException("Availability field must not be null");
        }

        if (itemDtoRequest.getName() == null || itemDtoRequest.getName().isBlank()) {
            throw new ValidationException("Item name must not be null");
        }

        if (itemDtoRequest.getDescription() == null || itemDtoRequest.getDescription().isBlank()) {
            throw new ValidationException("Item description must not be null");
        }

        if (userMemoryRepository.getUserById(userId) == null) {
            throw new NotFoundException("User with the given ID not found.");
        }

        Item item = itemMapper.mapToItem(itemDtoRequest);
        item.setOwner(userId);
        item.setId(idGenerator.getNextId(itemsMap));
        itemsMap.put(item.getId(), item);

        return itemMapper.mapToItemDto(item);
    }

    public ItemDto updateItem(Integer itemId, ItemDto itemDtoRequest, Integer userId) {
        Item item = Optional.ofNullable(itemsMap.get(itemId))
                .orElseThrow(() -> new NotFoundException("Item with the given ID not found."));

        if (!item.getOwner().equals(userId)) {
            throw new NotFoundException("Only the owner can modify the item.");
        }

        item.setName(itemDtoRequest.getName());
        item.setDescription(itemDtoRequest.getDescription());
        item.setAvailable(itemDtoRequest.getAvailable());

        return itemMapper.mapToItemDto(item);
    }

    public ItemDto getItem(Integer itemId) {
        return Optional.ofNullable(itemsMap.get(itemId))
                .map(itemMapper::mapToItemDto)
                .orElseThrow(() -> new NotFoundException("Item with the given ID not found."));
    }

    public List<ItemDto> getOwnerItems(Integer ownerId) {
        Collection<Item> allItems = itemsMap.values();
        List<ItemDto> ownerItemsList = new ArrayList<>();

        allItems.forEach(item -> {
            if (Objects.equals(item.getOwner(), ownerId)) {
                ownerItemsList.add(itemMapper.mapToItemDto(item));
            }
        });

        if (ownerItemsList.isEmpty()) {
            throw new NotFoundException("User's items not found.");
        }

        return ownerItemsList;
    }

    public List<ItemDto> itemSearch(String text) {
        List<ItemDto> searchItems = new ArrayList<>();
        if (text != null && !text.isBlank()) {

            Collection<Item> allItems = itemsMap.values();

            allItems.forEach(item -> {
                if (item.getName() != null && item.getAvailable() != null && item.getAvailable().equals(true)) {
                    if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                        searchItems.add(itemMapper.mapToItemDto(item));
                    }
                }

                if (item.getDescription() != null && item.getAvailable() != null && item.getAvailable().equals(true)) {
                    if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                        searchItems.add(itemMapper.mapToItemDto(item));
                    }
                }
            });
        }
        return searchItems;
    }
}