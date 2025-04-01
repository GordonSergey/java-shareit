package ru.practicum.shareit.service.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingDto validBookingDto;
    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(2L);

        validBookingDto = new BookingDto();
        validBookingDto.setStart(LocalDateTime.now().plusDays(1));
        validBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        validBookingDto.setItemId(item.getId());

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    void createBooking_shouldThrowIfUserNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.createBooking(user.getId(), validBookingDto));
    }

    @Test
    void createBooking_shouldThrowIfItemNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.existsById(item.getId())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.createBooking(user.getId(), validBookingDto));
    }

    @Test
    void createBooking_shouldThrowIfItemNotAvailable() {
        item.setAvailable(false);

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.existsById(item.getId())).thenReturn(true);
        when(itemRepository.getById(item.getId())).thenReturn(item);

        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(user.getId(), validBookingDto));
    }

    @Test
    void createBooking_shouldThrowIfStartIsInPast() {
        validBookingDto.setStart(LocalDateTime.now().minusDays(1));

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.existsById(item.getId())).thenReturn(true);
        when(itemRepository.getById(item.getId())).thenReturn(item);

        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(user.getId(), validBookingDto));
    }

    @Test
    void createBooking_shouldThrowIfEndBeforeStart() {
        validBookingDto.setEnd(validBookingDto.getStart().minusHours(1));

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.existsById(item.getId())).thenReturn(true);
        when(itemRepository.getById(item.getId())).thenReturn(item);

        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(user.getId(), validBookingDto));
    }

    @Test
    void setBookingApproval_shouldApproveBooking() {
        booking.setStatus(BookingStatus.WAITING);
        item.setOwner(1L);
        booking.setItem(item);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Booking result = bookingService.setBookingApproval(1L, booking.getId(), true);

        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void setBookingApproval_shouldRejectBooking() {
        booking.setStatus(BookingStatus.WAITING);
        item.setOwner(1L);
        booking.setItem(item);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Booking result = bookingService.setBookingApproval(1L, booking.getId(), false);

        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void setBookingApproval_shouldThrowIfAlreadyApproved() {
        booking.setStatus(BookingStatus.APPROVED);
        item.setOwner(1L);
        booking.setItem(item);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class,
                () -> bookingService.setBookingApproval(1L, booking.getId(), true));
    }

    @Test
    void setBookingApproval_shouldThrowIfNotOwner() {
        item.setOwner(999L);
        booking.setItem(item);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class,
                () -> bookingService.setBookingApproval(1L, booking.getId(), true));
    }
}