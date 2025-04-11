package ru.practicum.shareit.service.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.CommentService;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemServiceImplTest {

    private ItemRepository itemRepository;
    private UserService userService;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private CommentService commentService;
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        userService = mock(UserService.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        commentService = mock(CommentService.class);

        itemService = new ItemServiceImpl(itemRepository, userService, bookingRepository, commentRepository, commentService);
    }

    @Test
    void updateItemNotFound_ThrowsException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                itemService.update(1L, 1L, new ItemDto()));
    }

    @Test
    void updateWrongOwner_ThrowsException() {
        Item item = new Item();
        item.setId(1L);
        item.setOwner(99L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ResourceNotFoundException.class, () ->
                itemService.update(1L, 1L, new ItemDto()));
    }

    @Test
    void saveItemWithRequestId_Success() {
        ItemDto dto = new ItemDto();
        dto.setName("name");
        dto.setDescription("desc");
        dto.setAvailable(true);
        dto.setRequestId(3L);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("User");

        when(userService.getUserById(1L)).thenReturn(userDto);

        when(itemRepository.save(any())).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            savedItem.setId(100L); // назначаем id вручную, чтобы избежать NPE
            return savedItem;
        });

        when(commentRepository.findAllByItemId(anyLong())).thenReturn(Collections.emptyList());

        ItemDto result = itemService.saveItem(1L, dto);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getRequestId(), result.getRequestId());
    }

    @Test
    void saveItemUserNotFound_ThrowsException() {
        when(userService.getUserById(1L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                itemService.saveItem(1L, new ItemDto()));
    }

    @Test
    void searchItems_BlankText_ReturnsEmptyList() {
        List<ItemDto> result = itemService.searchItems("  ");
        assertTrue(result.isEmpty());
    }

    @Test
    void addComment_ItemNotFound_ThrowsException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                itemService.addComment(1L, 1L, "text"));
    }

    @Test
    void addComment_Success() {
        Item item = new Item();
        item.setId(1L);
        item.setComments(new ArrayList<>());

        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("user");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userService.getUserById(1L)).thenReturn(user);

        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                eq(1L), eq(1L), eq(BookingStatus.APPROVED), any(LocalDateTime.class))
        ).thenReturn(true);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("text");

        when(commentRepository.save(any())).thenReturn(comment);

        var result = itemService.addComment(1L, 1L, "text");

        assertEquals("text", result.getText());
        assertEquals("user", result.getAuthorName());
    }

    @Test
    void addComment_WithoutBooking_ShouldThrowException() {
        Item item = new Item();
        item.setId(1L);
        item.setComments(new ArrayList<>());

        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("user");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userService.getUserById(1L)).thenReturn(user);

        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                eq(1L), eq(1L), eq(BookingStatus.APPROVED), any(LocalDateTime.class))
        ).thenReturn(false);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> itemService.addComment(1L, 1L, "text"));

        assertEquals("A comment can only be left after the booking is completed.", ex.getMessage());
    }

    @Test
    void getItemById_NotOwner_SetsNullBookings() {
        Item item = new Item();
        item.setId(1L);
        item.setOwner(99L);
        item.setComments(Collections.emptyList());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentService.getNameAuthorByCommentId(anyLong())).thenReturn("author");

        ItemDto result = itemService.getItemById(1L, 1L);

        assertNull(result.getNextBooking());
        assertNull(result.getLastBooking());
    }

    @Test
    void getItemById_Owner_SetsBookings() {
        Item item = new Item();
        item.setId(1L);
        item.setOwner(1L);
        item.setComments(Collections.emptyList());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentService.getNameAuthorByCommentId(anyLong())).thenReturn("author");

        ItemDto result = itemService.getItemById(1L, 1L);

        assertNotNull(result);
    }

    @Test
    void updateItem_UpdatesFieldsCorrectly() {
        Item item = new Item();
        item.setId(1L);
        item.setOwner(1L);

        ItemDto dto = new ItemDto();
        dto.setName("updated");
        dto.setDescription("new desc");
        dto.setAvailable(false);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ItemDto result = itemService.update(1L, 1L, dto);

        assertEquals("updated", result.getName());
        assertEquals("new desc", result.getDescription());
        assertEquals(false, result.getAvailable());
    }
}