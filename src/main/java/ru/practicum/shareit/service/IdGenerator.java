package ru.practicum.shareit.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IdGenerator {

    public Integer getNextId(Map<Integer, ?> data) {
        return data.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }
}