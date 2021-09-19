package ru.cbrrate.services.processors;

import java.time.LocalDateTime;

public interface DateTimeProvider {
    LocalDateTime get();
}
