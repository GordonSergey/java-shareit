package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.intf.Create;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(groups = Create.class, message = "Name cannot be empty")
    private String name;

    @NotBlank(groups = Create.class, message = "Description cannot be empty")
    private String description;

    @NotNull(groups = Create.class, message = "Availability status cannot be empty")
    private Boolean available;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments;
}