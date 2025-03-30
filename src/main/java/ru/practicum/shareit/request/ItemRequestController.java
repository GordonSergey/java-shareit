package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestBody ItemRequestDto requestDto) {
        return ResponseEntity.ok(requestService.createRequest(userId, requestDto));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestWithResponsesDto>> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(requestService.getOwnRequests(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestWithResponsesDto>> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(requestService.getAllRequests(userId));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestWithResponsesDto> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                      @PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.getRequestById(userId, requestId));
    }
}