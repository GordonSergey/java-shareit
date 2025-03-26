package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.intf.Create;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private long id;

    @FutureOrPresent(groups = Create.class, message = "The booking start time must be in the future")
    @NotNull(groups = Create.class, message = "Time cannot be empty")
    private LocalDateTime start;

    @Future(groups = Create.class, message = "The booking end time must be in the future")
    @NotNull(groups = Create.class, message = "Time cannot be empty")
    private LocalDateTime end;

    @NotNull(groups = Create.class, message = "The item cannot be empty")
    private long itemId;

    private long bookerId;

    private BookingStatus status;
}