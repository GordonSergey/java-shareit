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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Интеграционные тесты user контроллера")
public class UserControllerIT {

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
    @DisplayName("Корректное возвращение списка пользователей")
    void findUsers_ReturnsUsersList() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/users")
                .header("X-Sharer-User-Id", 1L);

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Тестовый Пользователь"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Тестовый Пользователь 2"))
                .andExpect(jsonPath("$[1].email").value("test2@example.com"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("Иван Иванов"))
                .andExpect(jsonPath("$[2].email").value("ivan@example.com"))
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[3].name").value("Петр Петров"))
                .andExpect(jsonPath("$[3].email").value("petr@example.com"));
    }

}