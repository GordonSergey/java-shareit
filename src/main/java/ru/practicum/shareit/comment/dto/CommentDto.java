package ru.practicum.shareit.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.intf.Create;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank(groups = Create.class, message = "Text cannot be empty")
    private String text;

    @NotBlank(groups = Create.class, message = "Name cannot be empty")
    private String authorName;

    @NotNull(message = "Comment creation date cannot be empty")
    private LocalDateTime created;
}