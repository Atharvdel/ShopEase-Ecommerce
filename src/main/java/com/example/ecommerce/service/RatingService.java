package com.example.ecommerce.service;

import com.example.ecommerce.model.Rating;
import com.example.ecommerce.model.RatingId;
import java.util.List;
import java.util.Optional;

public interface RatingService {
    Rating saveRating(Rating rating);
    List<Rating> getAllRatings();
    Optional<Rating> getRatingById(RatingId id);
    List<Rating> getRatingsByProductId(Long productId);
    Double getAverageRatingForProduct(Long productId);
    void deleteRating(RatingId id);
    Optional<Rating> getRatingByCustomerAndProduct(Long customerId, Long productId);
}
