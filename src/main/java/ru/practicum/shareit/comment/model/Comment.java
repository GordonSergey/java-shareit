package ru.practicum.shareit.comment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Comment text cannot be empty")
    @Column(name = "text")
    private String text;

    @NotNull(message = "Item ID cannot be empty")
    @Column(name = "item_id")
    private Long itemId;

    @NotNull(message = "Author ID cannot be empty")
    @Column(name = "author_id")
    private Long authorId;

    @NotNull(message = "Comment creation date cannot be empty")
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}