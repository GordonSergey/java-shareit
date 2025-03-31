package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentItemRequestDto;
import ru.practicum.shareit.intf.Create;
import ru.practicum.shareit.intf.Update;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private static final String USER_ID = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(USER_ID) long userId,
                                              @PathVariable long itemId) {
        log.info("GATEWAY: Получение предмета по ID: {}, userId={}", itemId, userId);
        return itemClient.getItemById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestHeader(USER_ID) long userId,
                                           @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("GATEWAY: Добавление предмета пользователем с ID: {}", userId);
        return itemClient.saveItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID) long userId,
                                             @PathVariable long itemId,
                                             @Validated(Update.class) @RequestBody ItemDto itemDto) {
        log.info("GATEWAY: Обновление предмета {} пользователем с ID: {}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> findItemsByOwner(@RequestHeader(USER_ID) long userId) {
        log.info("GATEWAY: Получение всех предметов пользователя с ID: {}", userId);
        return itemClient.findItemsByOwner(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String searchText) {
        log.info("GATEWAY: Поиск предметов по тексту: {}", searchText);
        return itemClient.searchItems(searchText);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID) long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody CommentItemRequestDto commentDto) {
        log.info("GATEWAY: Добавление комментария к предмету {}, от пользователя {}", itemId, userId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}