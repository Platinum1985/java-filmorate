package ru.yandex.practicum.homeTheatre.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> allUsers = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return allUsers.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Начинается создание нового user: {}", user);
        // проверяем выполнение необходимых условий
        if (validateUser(user)) {
            log.info("Валидация пользователя {} прошла успешно", user);
            // формируем дополнительные данные
            user.setId(getNextId());
            log.info("Присвоили id {} для пользователя {}", user.getId(), user);
            //если имя пустое-приравняем имя к Login
            if (!StringUtils.hasText(user.getName())) {
                log.debug("Имя добавляемого пользователя {} пустое", user);
                user.setName(user.getLogin());
                log.debug("Имя пользователя {} приравняли логину", user);
            }
            // сохраняем новую публикацию в памяти приложения
            allUsers.put(user.getId(), user);
            log.info("Добавление пользователя в HashMap");
        } else {
            log.error("Некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        }
        log.info("Пользователь {} успешно создан и добавлен в HashMap", user);
        return user;

    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Начинается обновление пользователя: {}", user);
        // проверяем необходимые условия
        if (!exists(user)) {
            log.error("Пользователь с ID {} не найден", user.getId());
            throw new NoFoundIdException("Пост с id = " + user.getId() + " не найден");
        }
        if (!validateUser(user)) {
            log.error("Пользователь с ID {} не найден", user.getId());
            throw new ValidationException("некорректно заполнены поля");
        }
        //если имя пустое-приравняем имя к Login
        if (!StringUtils.hasText(user.getName())) {
            log.debug("Имя пользователя {} пустое", user);
            user.setName(user.getLogin());
            log.debug("Имя изменяемого пользователя {} приравняли логину", user);
        }
        allUsers.put(user.getId(), user);
        log.info("Изменили данные пользователя");
        return user;
    }

    private int getNextId() {
        int currentMaxId = Math.toIntExact(allUsers.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0));
        int nextId = ++currentMaxId;
        log.debug("Следующий доступный ID для фильма: {}", nextId);
        return nextId;
    }

    boolean exists(User user) {
        return allUsers.containsKey(user.getId());
    }

    public boolean validateUser(User user) {
        // Проверка электронной почты
        if (!StringUtils.hasText(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("Не заполнено email или заполнен некорректно");
            return false;
        }

        // Проверка логина
        if (!StringUtils.hasText(user.getLogin()) || user.getLogin().contains(" ")) {
            log.error("Не заполнен login или заполнен некорректно");
            return false;
        }

        // Дата рождения не может быть в будущем
        LocalDate today = LocalDate.now();
        if (user.getBirthday() == null || user.getBirthday().isAfter(today)) {
            log.error("Не заполнена дата или заполнен некорректно");
            return false;
        }

        return true; // Все проверки пройдены успешно
    }
}
