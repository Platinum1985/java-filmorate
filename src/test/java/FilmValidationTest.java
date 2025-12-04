import org.junit.jupiter.api.Test;
import ru.yandex.practicum.homeTheatre.model.Film;
import ru.yandex.practicum.homeTheatre.controller.FilmController;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmValidationTest {
    @Test
    void testValidateFilmWithEmptyName() {
        Film film = new Film();
        film.setName("");
        assertFalse(FilmController.validateFilm(film));
    }

    @Test
    void testValidateFilmWithLongDescription() {
        Film film = new Film();
        film.setDescription("a".repeat(201));
        assertFalse(FilmController.validateFilm(film));
    }

    @Test
    void testValidateFilmWithInvalidReleaseDate() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertFalse(FilmController.validateFilm(film));
    }

    @Test
    void testValidateFilmWithNegativeDuration() {
        Film film = new Film();
        film.setDuration(-1L);
        assertFalse(FilmController.validateFilm(film));
    }

    @Test
    void testValidFilm() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1896, 1, 1));
        film.setDuration(120L);
        assertTrue(FilmController.validateFilm(film));
    }
}
