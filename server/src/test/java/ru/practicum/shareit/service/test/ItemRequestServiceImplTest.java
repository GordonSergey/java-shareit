package ru.practicum.shareit.service.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl service;

    private User user;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "user", "user@mail.com");
        request = ItemRequest.builder()
                .id(1L)
                .description("desc")
                .requester(user)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void createRequest_success() {
        ItemRequestDto dto = new ItemRequestDto(null, "desc", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.save(any())).thenReturn(request);

        ItemRequestDto result = service.createRequest(1L, dto);

        assertEquals("desc", result.getDescription());
        verify(requestRepository).save(any());
    }

    @Test
    void getOwnRequests_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterIdOrderByCreatedDesc(1L)).thenReturn(List.of(request));
        when(itemRepository.findByRequestId(1L)).thenReturn(Collections.emptyList());

        List<ItemRequestWithResponsesDto> result = service.getOwnRequests(1L);

        assertEquals(1, result.size());
        assertEquals("desc", result.get(0).getDescription());
    }

    @Test
    void getAllRequests_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(1L)).thenReturn(List.of(request));
        when(itemRepository.findByRequestId(1L)).thenReturn(Collections.emptyList());

        List<ItemRequestWithResponsesDto> result = service.getAllRequests(1L);

        assertEquals(1, result.size());
        assertEquals("desc", result.get(0).getDescription());
    }

    @Test
    void getRequestById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequestId(1L)).thenReturn(Collections.emptyList());

        ItemRequestWithResponsesDto result = service.getRequestById(1L, 1L);

        assertEquals("desc", result.getDescription());
    }

    @Test
    void getRequestById_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getRequestById(1L, 1L));
    }

    @Test
    void getRequestById_requestNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getRequestById(1L, 1L));
    }
}