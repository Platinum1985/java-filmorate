package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.homeTheatre.dal.MPARepository;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.model.MPA;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService {
    private final MPARepository mpaRepository;

    public Collection<MPA> getAllMPA() {
        return mpaRepository.findAll();
    }

    public MPA getMPAById(int id) {
        return mpaRepository.findMPAById(id)
                .orElseThrow(() -> new NoFoundIdException("Жанр не найден с ID: " + id));
    }
}