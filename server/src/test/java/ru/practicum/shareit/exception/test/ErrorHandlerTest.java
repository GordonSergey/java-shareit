package ru.practicum.shareit.exception.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    @DisplayName("Должен вернуть 400 Bad Request при ValidationException")
    void handleValidationException_shouldReturnBadRequest() {
        ValidationException exception = new ValidationException("Неверные данные");

        ResponseEntity<ErrorMessage> response = errorHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Неверные данные", response.getBody().getError());
    }

    @Test
    @DisplayName("Должен вернуть 404 Not Found при ResourceNotFoundException")
    void handleNotFoundException_shouldReturnNotFound() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Не найдено");

        ResponseEntity<ErrorMessage> response = errorHandler.handleNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Не найдено", response.getBody().getError());
    }

    @Test
    @DisplayName("Должен вернуть 409 Conflict при ConflictException")
    void handleConflictException_shouldReturnConflict() {
        ConflictException exception = new ConflictException("Конфликт");

        ResponseEntity<ErrorMessage> response = errorHandler.handleConflictException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Конфликт", response.getBody().getError());
    }
}