package ru.practicum.shareit.service;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    public Integer getNextId(Map<Integer, ?> data) {
        return data.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }
}