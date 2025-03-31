package ru.practicum.shareit.service.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.CommentService;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentService commentService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveItem_shouldReturnSavedItemDto() {
        long userId = 1L;
        ItemDto inputDto = new ItemDto();
        inputDto.setName("Test Item");
        inputDto.setDescription("Test Desc");
        inputDto.setAvailable(true);

        Item savedItem = new Item();
        savedItem.setId(100L);
        savedItem.setName("Test Item");
        savedItem.setDescription("Test Desc");
        savedItem.setAvailable(true);
        savedItem.setOwner(userId);

        when(userService.getUserById(userId)).thenReturn(new UserDto());
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);
        when(commentRepository.findAllByItemId(savedItem.getId())).thenReturn(List.of());

        ItemDto result = itemService.saveItem(userId, inputDto);

        assertNotNull(result);
        assertEquals(savedItem.getId(), result.getId());
        assertEquals("Test Item", result.getName());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void testUpdate_shouldUpdateFields() {
        long userId = 1L;
        long itemId = 200L;

        Item existing = new Item();
        existing.setId(itemId);
        existing.setOwner(userId);
        existing.setName("Old");
        existing.setDescription("Old Desc");
        existing.setAvailable(false);

        ItemDto updateDto = new ItemDto();
        updateDto.setName("New");
        updateDto.setDescription("New Desc");
        updateDto.setAvailable(true);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existing));

        ItemDto result = itemService.update(userId, itemId, updateDto);

        assertEquals("New", result.getName());
        assertEquals("New Desc", result.getDescription());
        assertTrue(result.getAvailable());
    }
}