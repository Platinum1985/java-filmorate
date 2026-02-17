package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.dal.*;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.Review;
import ru.yandex.practicum.homeTheatre.model.ReviewLike;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    public void addReview(Review review) {
        if (!validateReview(review)) {
            log.error("некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");
        } else {
            log.info("Валидация review {} прошла успешно", review);
            reviewRepository.save(review);
        }
    }

    public void updateReview(Review review) {
        if (!validateReview(review) && review.getReviewId() == 0) {
            log.error("некорректно заполнены поля обновляемого объекта");
            throw new ValidationException("некорректно заполнены поля");
        } else {
            reviewRepository.update(review);
        }
    }

    public Review getReviewByReviewId(int reviewId) {
        return reviewRepository.getReviewById(reviewId);
    }

    public List<Review> getAllReviews(int count) {
        return reviewRepository.findAllReviews(count);
    }

    public List<Review> getReviewsByFilmId(int filmId, int count) {
        return reviewRepository.findAllReviewsByFilmId(filmId, count);
    }

    public void removeReview(int id) {
        reviewRepository.deleteReview(id);
    }

    public void addReviewLike(int reviewId, int userId) {
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.setLike(true);
        reviewLike.setReviewId(reviewId);
        reviewLike.setUserId(userId);

        if (reviewLikeRepository.addLikeOrDislike(reviewLike)) { // если лайк добавлен, увеличиваем значение поля useful на 1
            reviewRepository.increaseUseful(reviewId);
        }
    }

    public void addReviewDislike(int reviewId, int userId) {
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.setLike(false);
        reviewLike.setReviewId(reviewId);
        reviewLike.setUserId(userId);

        if (reviewLikeRepository.addLikeOrDislike(reviewLike)) { // если лайк добавлен, увеличиваем значение поля useful на 1
            reviewRepository.decreaseUseful(reviewId);
        }
    }

    public void removeReviewLike(int reviewId, int userId) {
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.setLike(true);
        reviewLike.setReviewId(reviewId);
        reviewLike.setUserId(userId);
        if (reviewLikeRepository.deleteLikeOrDislike(reviewLike)) {
            reviewRepository.decreaseUseful(reviewId); // при удалении лайка уменьшаем useful на 1
        }
    }

    public void removeReviewDislike(int reviewId, int userId) {
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.setLike(false);
        reviewLike.setReviewId(reviewId);
        reviewLike.setUserId(userId);
        if (reviewLikeRepository.deleteLikeOrDislike(reviewLike)) {
            reviewRepository.increaseUseful(reviewId); // при удалении дизлайка useful +1
        }
    }

    public static boolean validateReview(Review review) {
        // Проверка, что название не пустое
        if (!StringUtils.hasText(review.getContent())) {
            log.error("Не заполнено или пустое поле name");
            return false;
        }
        // Проверка isPositive
        if (review.getIsPositive() == null) {
            log.error("поле isPositive не должно быть пустым");
            return false;
        }

        // Проверка userId
        if (review.getUserId() <= 0) {
            log.error("userId должен быть положительным числом");
            return false;
        }

        // Проверка filmId
        if (review.getFilmId() <= 0) {
            log.error("filmId должен быть положительным числом");
            return false;
        }
        return true; // Все проверки пройдены успешно!
    }

}

