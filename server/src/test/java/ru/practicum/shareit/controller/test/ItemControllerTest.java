package ru.practicum.shareit.controller.test;

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
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-тесты ItemController")
public class ItemControllerTest {

    @Mock
    ItemService itemService;

    @InjectMocks
    ItemController itemController;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Получение предмета по ID - успешный сценарий")
    void getItemById_ValidRequest_ReturnsOk() throws Exception {
        var userId = 1L;
        var itemId = 1L;

        var expectedItem = new ItemDto();
        expectedItem.setId(itemId);
        expectedItem.setName("Test Item");

        doReturn(expectedItem)
                .when(itemService)
                .getItemById(userId, itemId);

        var requestBuilder = MockMvcRequestBuilders.get("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(itemId),
                        jsonPath("$.name").value("Test Item")
                );

        verify(itemService).getItemById(userId, itemId);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    @DisplayName("Создание предмета - успешный сценарий")
    void saveItem_ValidRequest_ReturnsCreated() throws Exception {
        var userId = 1L;
        var itemDto = new ItemDto();
        itemDto.setName("New Item");
        itemDto.setDescription("Item description");
        itemDto.setAvailable(true);

        var savedItem = new ItemDto();
        savedItem.setId(1L);
        savedItem.setName(itemDto.getName());

        doReturn(savedItem)
                .when(itemService)
                .saveItem(userId, itemDto);

        var requestBuilder = MockMvcRequestBuilders.post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto));

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.name").value("New Item")
                );

        verify(itemService).saveItem(userId, itemDto);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    @DisplayName("Обновление предмета - успешный сценарий")
    void update_ValidRequest_ReturnsOk() throws Exception {
        var userId = 1L;
        var itemId = 1L;

        var updateDto = new ItemDto();
        updateDto.setName("Updated Name");

        var updatedItem = new ItemDto();
        updatedItem.setId(itemId);
        updatedItem.setName("Updated Name");

        doReturn(updatedItem)
                .when(itemService)
                .update(userId, itemId, updateDto);

        var requestBuilder = MockMvcRequestBuilders.patch("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto));

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(itemId),
                        jsonPath("$.name").value("Updated Name")
                );

        verify(itemService).update(userId, itemId, updateDto);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    @DisplayName("Получение предметов владельца - успешный сценарий")
    void findItemsByOwner_ValidRequest_ReturnsOk() throws Exception {
        var userId = 1L;

        var item1 = new ItemDto();
        item1.setId(1L);
        item1.setName("Item 1");

        var item2 = new ItemDto();
        item2.setId(2L);
        item2.setName("Item 2");

        var items = List.of(item1, item2);

        doReturn(items)
                .when(itemService)
                .findItemsByOwner(userId);

        var requestBuilder = MockMvcRequestBuilders.get("/items")
                .header("X-Sharer-User-Id", userId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].name").value("Item 1"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].name").value("Item 2")
                );

        verify(itemService).findItemsByOwner(userId);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    @DisplayName("Поиск предметов - успешный сценарий")
    void searchItems_ValidRequest_ReturnsOk() throws Exception {
        var searchText = "test";

        var item1 = new ItemDto();
        item1.setId(1L);
        item1.setName("Test Item 1");

        var item2 = new ItemDto();
        item2.setId(2L);
        item2.setName("Test Item 2");

        var items = List.of(item1, item2);

        doReturn(items)
                .when(itemService)
                .searchItems(searchText);

        var requestBuilder = MockMvcRequestBuilders.get("/items/search")
                .param("text", searchText);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].name").value("Test Item 1"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].name").value("Test Item 2")
                );

        verify(itemService).searchItems(searchText);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    @DisplayName("Добавление комментария - успешный сценарий")
    void addComment_ValidRequest_ReturnsOk() throws Exception {
        var userId = 1L;
        var itemId = 1L;

        var commentDto = new CommentDto();
        commentDto.setText("Great item!");

        var savedComment = new CommentDto();
        savedComment.setId(1L);
        savedComment.setText("Great item!");

        doReturn(savedComment)
                .when(itemService)
                .addComment(userId, itemId, commentDto.getText());

        var requestBuilder = MockMvcRequestBuilders.post("/items/{itemId}/comment", itemId)
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto));

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.text").value("Great item!")
                );

        verify(itemService).addComment(userId, itemId, commentDto.getText());
        verifyNoMoreInteractions(itemService);
    }
}