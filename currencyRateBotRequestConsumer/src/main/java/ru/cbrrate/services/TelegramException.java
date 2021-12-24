package ru.cbrrate.services;

public class TelegramException extends RuntimeException {
    public TelegramException(Throwable cause) {
        super(cause);
    }

    public TelegramException(String message) {
        super(message);
    }
}
