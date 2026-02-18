package ru.yandex.practicum.homeTheatre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.Review;
import ru.yandex.practicum.homeTheatre.service.ReviewService;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public Review create(@RequestBody Review review) {
        log.info("Полученный отзыв: " + review.toString());
        reviewService.addReview(review);
        return review;
    }

    @PutMapping("/reviews")
    public Review update(@RequestBody Review review) {
        reviewService.updateReview(review);
        return review;
    }

    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@PathVariable("id") int id) {
        reviewService.removeReview(id);
    }

    @GetMapping("/reviews/{id}")
    public Review getReview(@PathVariable("id") int id) {
        return reviewService.getReviewByReviewId(id);
    }

    @GetMapping("/reviews")
    public Collection<Review> getPopularReviews(@RequestParam(value = "filmId", required = false) Integer filmId,
                                                @RequestParam(value = "count", defaultValue = "10") int count) {
        if (filmId == null) {
            return reviewService.getAllReviews(count);
        } else {
            return reviewService.getReviewsByFilmId(filmId, count);

        }
    }

    @PutMapping("/reviews/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int reviewId, @PathVariable("userId") int userId) {
        reviewService.addReviewLike(reviewId, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}")
    public void addDislike(@PathVariable("id") int reviewId, @PathVariable("userId") int userId) {
        reviewService.addReviewDislike(reviewId, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int reviewId, @PathVariable("userId") int userId) {
        reviewService.removeReviewLike(reviewId, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable("id") int reviewId, @PathVariable("userId") int userId) {
        reviewService.removeReviewDislike(reviewId, userId);
    }

    @ExceptionHandler(NoFoundIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoFoundIdException(NoFoundIdException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // код 400
    public Map<String, String> handleValidationException(ValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneralException(Exception e) {
        return Map.of("error", "Произошла внутренняя ошибка сервера.");
    }
}

