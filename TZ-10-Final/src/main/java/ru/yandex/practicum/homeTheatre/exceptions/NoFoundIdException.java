package ru.yandex.practicum.homeTheatre.exceptions;

public class NoFoundIdException extends RuntimeException {
    public NoFoundIdException(String message) {
        super(message);
    }
}
