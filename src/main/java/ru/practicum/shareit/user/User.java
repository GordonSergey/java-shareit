package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private Integer id;
    private String name;
    private String email;

    @Getter
    @Setter
    public static class UserDto {
        private Integer id;
        private String name;
        private String email;

        public boolean hasEmail() {
            return email != null && !email.isEmpty();
        }

        public boolean hasName() {
            return name != null && !name.isEmpty();
        }
    }
}