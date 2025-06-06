package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.CommentService;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @Override
    public List<ItemDto> findItemsByOwner(long userId) {
        List<Item> items = itemRepository.findItemsByOwner(userId);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            ItemDto b = ItemMapper.mapToItemDto(item);
            b.setNextBooking(findNextBookingByItemId(item.getId()));
            b.setLastBooking(findLastBookingByItemId(item.getId()));
            itemDtos.add(b);
        }
        return itemDtos;
    }

    @Override
    @Transactional
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));

        if (item.getOwner() != userId) {
            throw new ResourceNotFoundException("Отсутствует user под id");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto getItemById(long userId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new ResourceNotFoundException("Item not found with ID: " + itemId));
        ItemDto itemDto = ItemMapper.mapToItemDto(item);

        if (item.getOwner() != userId) {
            itemDto.setNextBooking(null);
            itemDto.setLastBooking(null);
            List<CommentDto> commentDtos = getNameAuthor(item);
            itemDto.setComments(commentDtos);
        } else {
            itemDto.setNextBooking(findNextBookingByItemId(itemId));
            itemDto.setLastBooking(findLastBookingByItemId(itemId));
            List<CommentDto> commentDtos = getNameAuthor(item);
            itemDto.setComments(commentDtos);
        }
        return itemDto;
    }

    private List<CommentDto> getNameAuthor(Item item) {
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : item.getComments()) {
            CommentDto commentDto = CommentMapper.mapToCommentDto(comment);
            String authorName = commentService.getNameAuthorByCommentId(comment.getId());
            commentDto.setAuthorName(authorName);
            commentDtos.add(commentDto);
        }
        return commentDtos;
    }

    @Override
    @Transactional
    public ItemDto saveItem(long userId, ItemDto itemDto) {
        if (userService.getUserById(userId) == null) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        Item item = ItemMapper.mapToNewItem(itemDto);
        item.setOwner(userId);

        if (itemDto.getRequestId() != null) {
            ItemRequest request = new ItemRequest();
            request.setId(itemDto.getRequestId());
            item.setRequest(request);
        }

        item = itemRepository.save(item);

        ItemDto dto = ItemMapper.mapToItemDto(item);
        dto.setNextBooking(findNextBookingByItemId(item.getId()));
        dto.setLastBooking(findLastBookingByItemId(item.getId()));
        dto.setComments(CommentMapper.mapToCommentDto(commentRepository.findAllByItemId(item.getId())));
        return dto;
    }

    @Override
    public List<ItemDto> searchItems(String searchText) {
        if (searchText.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableIsTrueOrNameContainingIgnoreCaseAndAvailableIsTrue(searchText, searchText);
        return ItemMapper.mapToItemDto(items);
    }

    @Override
    public CommentDto addComment(long userId, long itemId, String text) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ResourceNotFoundException("Item not found with ID: " + itemId));

        UserDto user = userService.getUserById(userId);

        boolean hasCompletedBooking = bookingRepository
                .existsByItemIdAndBookerIdAndStatusAndEndBefore(
                        itemId, userId, BookingStatus.APPROVED, LocalDateTime.now()
                );

        if (!hasCompletedBooking) {
            throw new ValidationException("A comment can only be left after the booking is completed.");
        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setItemId(itemId);
        comment.setAuthorId(userId);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        List<Comment> comments = item.getComments();
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
        item.setComments(comments);

        CommentDto commentDto = CommentMapper.mapToCommentDto(comment);
        commentDto.setAuthorName(user.getName());

        return commentDto;
    }

    private BookingDto findLastBookingByItemId(long itemId) {
        Booking lastBookings = bookingRepository.findFirstBookingByItemIdAndStatusAndStartIsBefore(itemId,
                BookingStatus.APPROVED, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
        BookingDto lastBookingsDTO = BookingMapper.mapToBookingDto(lastBookings);
        return lastBookingsDTO;
    }

    private BookingDto findNextBookingByItemId(long itemId) {
        Booking nextBookings = bookingRepository.findFirstBookingByItemIdAndStatusAndStartIsAfter(itemId,
                BookingStatus.APPROVED, LocalDateTime.now(), Sort.by(Sort.Direction.ASC, "start"));
        BookingDto nextBookingsDTO = BookingMapper.mapToBookingDto(nextBookings);
        return nextBookingsDTO;
    }
}