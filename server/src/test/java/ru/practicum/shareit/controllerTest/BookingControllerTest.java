package ru.practicum.shareit.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-тесты BookingController")
public class BookingControllerTest {

    @Mock
    BookingService bookingService;
    @InjectMocks
    BookingController bookingController;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Cоздание нового booking")
    void createBooking_RequestIsValid_ReturnCreated() throws Exception {
        long id = 1L;

        var payload = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.APPROVED)
                .build();

        Booking expectedBooking = new Booking();
        expectedBooking.setId(1L);
        expectedBooking.setStatus(BookingStatus.WAITING);

        doReturn(expectedBooking)
                .when(this.bookingService)
                .createBooking(anyLong(), any(BookingDto.class));
        var requestBuilder = MockMvcRequestBuilders.post("/bookings")
                .header("X-Sharer-User-Id", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(payload));

        this.mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isCreated(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(1),
                        jsonPath("$.status").value("WAITING")
                );

        verify(this.bookingService).createBooking(id, payload);
        verifyNoMoreInteractions(this.bookingService);
    }

    @Test
    @DisplayName("Подтверждение бронирования - успешный сценарий")
    void setBookingApproval_RequestIsValid_ReturnOk() throws Exception {
        var userId = 1L;
        var bookingId = 1L;
        var approved = true;

        var expectedBooking = new Booking();
        expectedBooking.setId(bookingId);
        expectedBooking.setStatus(BookingStatus.APPROVED);

        doReturn(expectedBooking)
                .when(bookingService)
                .setBookingApproval(userId, bookingId, approved);

        var requestBuilder = MockMvcRequestBuilders.patch("/bookings/{bookingId}", bookingId)
                .header("X-Sharer-User-Id", userId)
                .param("approved", String.valueOf(approved));

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(bookingId),
                        jsonPath("$.status").value("APPROVED")
                );

        verify(bookingService).setBookingApproval(userId, bookingId, approved);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    @DisplayName("Получение бронирования по ID - успешный сценарий")
    void getBookingByIdAndBookerOrOwner_RequestIsValid_ReturnOk() throws Exception {
        var userId = 1L;
        var bookingId = 1L;

        var expectedBooking = new Booking();
        expectedBooking.setId(bookingId);
        expectedBooking.setStatus(BookingStatus.APPROVED);

        doReturn(expectedBooking)
                .when(bookingService)
                .getBookingByIdAndBookerOrOwner(bookingId, userId);

        var requestBuilder = MockMvcRequestBuilders.get("/bookings/{bookingId}", bookingId)
                .header("X-Sharer-User-Id", userId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(bookingId),
                        jsonPath("$.status").value("APPROVED")
                );

        verify(bookingService).getBookingByIdAndBookerOrOwner(bookingId, userId);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    @DisplayName("Получение всех бронирований пользователя - успешный сценарий")
    void findBookingsByBookerId_RequestIsValid_ReturnOk() throws Exception {
        var userId = 1L;

        var booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStatus(BookingStatus.APPROVED);

        var booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStatus(BookingStatus.WAITING);

        var expectedBookings = List.of(booking1, booking2);

        doReturn(expectedBookings)
                .when(bookingService)
                .findBookingsByBookerId(userId);

        var requestBuilder = MockMvcRequestBuilders.get("/bookings/")
                .header("X-Sharer-User-Id", userId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].status").value("APPROVED"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].status").value("WAITING")
                );

        verify(bookingService).findBookingsByBookerId(userId);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    @DisplayName("Получение бронирований владельца - успешный сценарий")
    void findBookingsByStateAndOwnerId_RequestIsValid_ReturnOk() throws Exception {
        var userId = 1L;
        var state = "ALL";

        var booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStatus(BookingStatus.APPROVED);

        var booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStatus(BookingStatus.REJECTED);

        var expectedBookings = List.of(booking1, booking2);

        doReturn(expectedBookings)
                .when(bookingService)
                .findBookingsByStateAndOwnerId(userId, state);

        var requestBuilder = MockMvcRequestBuilders.get("/bookings/owner")
                .header("X-Sharer-User-Id", userId)
                .param("state", state);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].status").value("APPROVED"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].status").value("REJECTED")
                );

        verify(bookingService).findBookingsByStateAndOwnerId(userId, state);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    @DisplayName("Получение бронирований арендатора с фильтром по состоянию - успешный сценарий")
    void findBookingsByStateAndBookerId_RequestIsValid_ReturnOk() throws Exception {
        var userId = 1L;
        var state = "FUTURE";

        var booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.WAITING);

        var expectedBookings = List.of(booking);

        doReturn(expectedBookings)
                .when(bookingService)
                .findBookingsByStateAndBookerId(userId, state);

        var requestBuilder = MockMvcRequestBuilders.get("/bookings")
                .header("X-Sharer-User-Id", userId)
                .param("state", state);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].status").value("WAITING")
                );

        verify(bookingService).findBookingsByStateAndBookerId(userId, state);
        verifyNoMoreInteractions(bookingService);
    }
}