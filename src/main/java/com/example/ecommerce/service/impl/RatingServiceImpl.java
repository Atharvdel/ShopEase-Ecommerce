package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Rating;
import com.example.ecommerce.model.RatingId;
import com.example.ecommerce.repository.RatingRepository;
import com.example.ecommerce.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public Optional<Rating> getRatingById(RatingId id) {
        return ratingRepository.findById(id);
    }

    @Override
    public List<Rating> getRatingsByProductId(Long productId) {
        return ratingRepository.findByProductId(productId);
    }

    @Override
    public Double getAverageRatingForProduct(Long productId) {
        return ratingRepository.getAverageRatingForProduct(productId);
    }

    @Override
    public void deleteRating(RatingId id) {
        ratingRepository.deleteById(id);
    }

    @Override
    public Optional<Rating> getRatingByCustomerAndProduct(Long customerId, Long productId) {
        RatingId ratingId = new RatingId(customerId, productId);
        return ratingRepository.findById(ratingId);
    }
}
