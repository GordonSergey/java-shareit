package ru.practicum.shareit.service.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.CommentServiceImpl;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.user.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getNameAuthorByCommentId: возвращает имя автора при валидном ID")
    void getNameAuthorByCommentId_validId_returnsUserName() {
        Long commentId = 1L;
        Long authorId = 2L;
        String expectedName = "John Doe";

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setAuthorId(authorId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.getUserNameById(authorId)).thenReturn(expectedName);

        String result = commentService.getNameAuthorByCommentId(commentId);

        assertEquals(expectedName, result);
        verify(commentRepository).findById(commentId);
        verify(userService).getUserNameById(authorId);
    }

    @Test
    @DisplayName("getNameAuthorByCommentId: выбрасывает ResourceNotFoundException при невалидном ID")
    void getNameAuthorByCommentId_invalidId_throwsException() {
        Long commentId = 99L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                commentService.getNameAuthorByCommentId(commentId));

        verify(commentRepository).findById(commentId);
        verifyNoInteractions(userService);
    }
}