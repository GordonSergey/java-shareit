package ru.practicum.shareit.mapper.test;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @Test
    void mapToCommentDto_shouldMapCorrectly() {
        // given
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.setCreatedAt(LocalDateTime.now());

        // when
        CommentDto dto = CommentMapper.mapToCommentDto(comment);

        // then
        assertNotNull(dto);
        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getText(), dto.getText());
        assertEquals(comment.getCreatedAt(), dto.getCreated());
    }

    @Test
    void mapToCommentDtoList_shouldMapAllElements() {
        // given
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("Comment 1");
        comment1.setCreatedAt(LocalDateTime.now());

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("Comment 2");
        comment2.setCreatedAt(LocalDateTime.now());

        List<Comment> commentList = List.of(comment1, comment2);

        // when
        List<CommentDto> dtoList = CommentMapper.mapToCommentDto(commentList);

        // then
        assertEquals(2, dtoList.size());

        assertEquals(comment1.getId(), dtoList.get(0).getId());
        assertEquals(comment2.getId(), dtoList.get(1).getId());
    }

    @Test
    void mapToCommentDtoList_shouldReturnEmptyList_whenInputIsEmpty() {
        // when
        List<CommentDto> dtoList = CommentMapper.mapToCommentDto(List.of());

        // then
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }
}