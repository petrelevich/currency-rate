package ru.cbrrate.services.processors;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DateTimeProviderCurrent implements DateTimeProvider {
    @Override
    public LocalDateTime get() {
        return LocalDateTime.now();
    }
}
