package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.intf.Create;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    private static final String USER_ID = "X-Sharer-User-Id";

    // POST /bookings
    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(USER_ID) long userId,
                                                @RequestBody @Validated(Create.class) BookingDto bookingDto) {
        log.info("POST /bookings: userId={}, booking={}", userId, bookingDto);
        return bookingClient.createBooking(userId, bookingDto);
    }

    // PATCH /bookings/{bookingId}?approved=true/false
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(USER_ID) long userId,
                                                 @PathVariable long bookingId,
                                                 @RequestParam("approved") boolean approved) {
        log.info("PATCH /bookings/{}?approved={}: userId={}", bookingId, approved, userId);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    // GET /bookings/{bookingId}
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID) long userId,
                                                 @PathVariable long bookingId) {
        log.info("GET /bookings/{}: userId={}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    // GET /bookings/
    @GetMapping("/")
    public ResponseEntity<Object> getBookingsByBooker(@RequestHeader(USER_ID) long userId) {
        log.info("GET /bookings/: userId={}", userId);
        return bookingClient.getBookingsByBooker(userId);
    }

    // GET /bookings/owner?state=ALL
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(USER_ID) long userId,
                                                     @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("GET /bookings/owner?state={}: userId={}", state, userId);
        return bookingClient.getBookingsByOwner(userId, state);
    }

    // GET /bookings?state=ALL
    @GetMapping
    public ResponseEntity<Object> getBookingsByState(@RequestHeader(USER_ID) long userId,
                                                     @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                     @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("GET /bookings?state={}: userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookingsByBookerAndState(userId, state);
    }
}