package ru.yandex.practicum.homeTheatre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.model.MPA;
import ru.yandex.practicum.homeTheatre.service.MPAService;


import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MPAController {
    private final MPAService mpaService;

    @GetMapping("/mpa")
    public Collection<MPA> getAllMPA() {
        return mpaService.getAllMPA();
    }

    @GetMapping("/mpa/{id}") // +
    public MPA findMPAById(@PathVariable("id") int id) {

        return mpaService.getMPAById(id);
    }

    @ExceptionHandler(NoFoundIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoFoundIdException(NoFoundIdException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneralException(Exception e) {
        log.error("Произошла ошибка на сервере", e);
        return Map.of("error", "Произошла внутренняя ошибка сервера.");
    }
}
