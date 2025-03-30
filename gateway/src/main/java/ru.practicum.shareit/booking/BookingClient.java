package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl,
                         RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .build());
    }

    // POST /bookings
    public ResponseEntity<Object> createBooking(long userId, BookingDto bookingDto) {
        return post("", userId, bookingDto);
    }

    // PATCH /bookings/{bookingId}?approved=true/false
    public ResponseEntity<Object> approveBooking(long userId, long bookingId, boolean approved) {
        Map<String, Object> params = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", userId, params, null);
    }

    // GET /bookings/{bookingId}
    public ResponseEntity<Object> getBookingById(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    // GET /bookings/
    public ResponseEntity<Object> getBookingsByBooker(long userId) {
        return get("/", userId);
    }

    // GET /bookings/owner?state=ALL
    public ResponseEntity<Object> getBookingsByOwner(long userId, String state) {
        return get("/owner?state=" + state, userId);
    }

    // GET /bookings?state=ALL
    public ResponseEntity<Object> getBookingsByBookerAndState(long userId, String state) {
        return get("?state=" + state, userId);
    }
}