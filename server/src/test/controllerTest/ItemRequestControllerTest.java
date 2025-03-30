package ru.practicum.shareit.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-тесты ItemRequestController")
public class ItemRequestControllerTest {

    @Mock
    ItemRequestService requestService;

    @InjectMocks
    ItemRequestController requestController;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(requestController)
                .setControllerAdvice() // Убираем валидацию
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Создание запроса на вещь - успешный сценарий")
    void createRequest_ValidRequest_ReturnsOk() throws Exception {
        // given
        var userId = 1L;
        var requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужна дрель");

        var createdRequest = new ItemRequestDto();
        createdRequest.setId(1L);
        createdRequest.setDescription("Нужна дрель");

        // Используем any() для requestDto, так как при сериализации/десериализации создается новый объект
        doReturn(createdRequest)
                .when(requestService)
                .createRequest(eq(userId), any(ItemRequestDto.class));

        // when
        var requestBuilder = MockMvcRequestBuilders.post("/requests")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto));

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.description").value("Нужна дрель")
                );

        verifyNoMoreInteractions(requestService);
    }

    @Test
    @DisplayName("Получение собственных запросов - успешный сценарий")
    void getOwnRequests_ValidRequest_ReturnsOk() throws Exception {
        // given
        var userId = 1L;

        var request1 = new ItemRequestWithResponsesDto();
        request1.setId(1L);
        request1.setDescription("Нужна дрель");

        var request2 = new ItemRequestWithResponsesDto();
        request2.setId(2L);
        request2.setDescription("Нужен шуруповерт");

        var requests = List.of(request1, request2);

        doReturn(requests)
                .when(requestService)
                .getOwnRequests(userId);

        // when
        var requestBuilder = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", userId);

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].description").value("Нужна дрель"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].description").value("Нужен шуруповерт")
                );

        verify(requestService).getOwnRequests(userId);
        verifyNoMoreInteractions(requestService);
    }

    @Test
    @DisplayName("Получение всех запросов - успешный сценарий")
    void getAllRequests_ValidRequest_ReturnsOk() throws Exception {
        // given
        var userId = 1L;

        var request1 = new ItemRequestWithResponsesDto();
        request1.setId(1L);
        request1.setDescription("Нужна дрель");

        var request2 = new ItemRequestWithResponsesDto();
        request2.setId(2L);
        request2.setDescription("Нужен шуруповерт");

        var requests = List.of(request1, request2);

        doReturn(requests)
                .when(requestService)
                .getAllRequests(userId);

        // when
        var requestBuilder = MockMvcRequestBuilders.get("/requests/all")
                .header("X-Sharer-User-Id", userId);

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].description").value("Нужна дрель"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].description").value("Нужен шуруповерт")
                );

        verify(requestService).getAllRequests(userId);
        verifyNoMoreInteractions(requestService);
    }

    @Test
    @DisplayName("Получение запроса по ID - успешный сценарий")
    void getRequestById_ValidRequest_ReturnsOk() throws Exception {
        // given
        var userId = 1L;
        var requestId = 1L;

        var request = new ItemRequestWithResponsesDto();
        request.setId(requestId);
        request.setDescription("Нужна дрель");

        doReturn(request)
                .when(requestService)
                .getRequestById(userId, requestId);

        // when
        var requestBuilder = MockMvcRequestBuilders.get("/requests/{requestId}", requestId)
                .header("X-Sharer-User-Id", userId);

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(requestId),
                        jsonPath("$.description").value("Нужна дрель")
                );

        verify(requestService).getRequestById(userId, requestId);
        verifyNoMoreInteractions(requestService);
    }

    @Test
    @DisplayName("Получение запросов без userId в заголовке - возвращает 400")
    void getOwnRequests_WithoutUserId_ReturnsBadRequest() throws Exception {
        // when
        var requestBuilder = MockMvcRequestBuilders.get("/requests");
        // Нет заголовка X-Sharer-User-Id

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verifyNoInteractions(requestService);
    }
}