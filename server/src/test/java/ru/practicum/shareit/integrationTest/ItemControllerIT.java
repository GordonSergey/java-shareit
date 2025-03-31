package ru.practicum.shareit.integrationTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Интеграционные тесты контроллера items")
public class ItemControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DataSource dataSource;

    @BeforeAll
    static void setupDatabase(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            String sqlScript = new String(Files.readAllBytes(
                    Paths.get("src/test/resources/sql/starting_db.sql")));
            statement.execute(sqlScript);
        } catch (IOException e) {
            throw new RuntimeException("Not found file");
        }
    }

    @Test
    @DisplayName("Создание нового предмета")
    void createItem_ReturnsNewItem() throws Exception {
        long userId = 1L;
        String itemJson = """
        {
            "name": "Новая дрель",
            "description": "Мощная аккумуляторная дрель",
            "available": true,
            "requestId": 1
        }
        """;

        var requestBuilder = MockMvcRequestBuilders.post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Новая дрель"))
                .andExpect(jsonPath("$.description").value("Мощная аккумуляторная дрель"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.requestId").value(1))
                .andExpect(jsonPath("$.lastBooking").doesNotExist())
                .andExpect(jsonPath("$.nextBooking").doesNotExist())
                .andExpect(jsonPath("$.comments").isEmpty());
    }

}