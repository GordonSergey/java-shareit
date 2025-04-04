package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        ItemRequest request = ItemRequest.builder()
                .description(requestDto.getDescription())
                .requester(user)
                .created(LocalDateTime.now())
                .build();

        return ItemRequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<ItemRequestWithResponsesDto> getOwnRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        return requestRepository.findAllByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(this::mapWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithResponsesDto> getAllRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        return requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId).stream()
                .map(this::mapWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestWithResponsesDto getRequestById(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        return mapWithItems(request);
    }

    private ItemRequestWithResponsesDto mapWithItems(ItemRequest request) {
        List<ItemDto> items = itemRepository.findByRequestId(request.getId()).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
        return ItemRequestMapper.toDtoWithItems(request, items);
    }
}