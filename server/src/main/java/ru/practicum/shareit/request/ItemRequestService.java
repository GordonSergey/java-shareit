package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto);

    List<ItemRequestWithResponsesDto> getOwnRequests(Long userId);

    List<ItemRequestWithResponsesDto> getAllRequests(Long userId);

    ItemRequestWithResponsesDto getRequestById(Long userId, Long requestId);
}