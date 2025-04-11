package ru.practicum.shareit.service.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ItemRequestServiceImplTest {

    private ItemRequestRepository requestRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private ItemRequestServiceImpl service;

    private User user;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        requestRepository = mock(ItemRequestRepository.class);
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);

        service = new ItemRequestServiceImpl(requestRepository, userRepository, itemRepository);

        user = new User(1L, "Test User", "test@example.com");

        request = ItemRequest.builder()
                .id(1L)
                .description("Need a drill")
                .requester(user)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void createRequest_shouldSaveAndReturnRequestDto() {
        Long userId = 1L;
        String description = "Need a ladder";
        ItemRequestDto requestDto = ItemRequestDto.builder().description(description).build();
        ItemRequest savedRequest = ItemRequest.builder().id(1L).description(description).requester(user).created(LocalDateTime.now()).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.save(any())).thenReturn(savedRequest);

        ItemRequestDto result = service.createRequest(userId, requestDto);

        assertThat(result.getDescription()).isEqualTo(description);
        verify(userRepository).findById(userId);
        verify(requestRepository).save(any(ItemRequest.class));
    }

    @Test
    void getOwnRequests_ReturnsEmptyList() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterIdOrderByCreatedDesc(1L)).thenReturn(Collections.emptyList());

        List<ItemRequestWithResponsesDto> result = service.getOwnRequests(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getOwnRequests_ThrowsIfUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getOwnRequests(99L));

        assertEquals("User not found: 99", ex.getMessage());
    }

    @Test
    void getAllRequests_ReturnsEmptyList() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(1L)).thenReturn(Collections.emptyList());

        List<ItemRequestWithResponsesDto> result = service.getAllRequests(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllRequests_UserNotFound_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.getAllRequests(99L));

        assertEquals("User not found: 99", exception.getMessage());
    }

    @Test
    void getRequestById_RequestNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.getRequestById(1L, 99L));

        assertEquals("Request not found: 99", exception.getMessage());
    }

    @Test
    void getRequestById_ReturnsRequestWithItems() {
        Long requestId = 1L;
        ItemDto itemDto = ItemDto.builder()
                .id(10L).name("item").description("desc").available(true).build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        Item item = Item.builder()
                .id(10L)
                .name("item")
                .description("desc")
                .available(true)
                .request(request)
                .build();

        when(itemRepository.findByRequestId(requestId)).thenReturn(List.of(item));

        ItemRequestWithResponsesDto result = service.getRequestById(user.getId(), requestId);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getName()).isEqualTo("item");
    }
}