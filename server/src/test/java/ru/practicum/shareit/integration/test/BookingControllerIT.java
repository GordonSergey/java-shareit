package ru.practicum.shareit.integration.test;

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
@DisplayName("Интеграционные тесты booking контроллера")
public class BookingControllerIT {

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
    @DisplayName("Корректное возвращение списка бронирований при запросе")
    void findBookings_ReturnsBookingList() throws Exception {
        long userId = 1L;
        var requestBuilder = MockMvcRequestBuilders.get("/bookings")
                .header("X-Sharer-User-Id", userId);

        this.mockMvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].start").exists())
                .andExpect(jsonPath("$[0].end").exists())
                .andExpect(jsonPath("$[0].item.id").value(3))
                .andExpect(jsonPath("$[0].item.name").value("Лестница"))
                .andExpect(jsonPath("$[0].booker.id").value(1))
                .andExpect(jsonPath("$[0].booker.name").value("Тестовый Пользователь"))
                .andExpect(jsonPath("$[0].status").value("REJECTED"));
    }
}