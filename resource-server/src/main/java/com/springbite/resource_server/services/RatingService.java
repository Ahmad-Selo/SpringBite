package com.springbite.resource_server.services;

import com.springbite.resource_server.exceptions.ResourceNotFoundException;
import com.springbite.resource_server.exceptions.ValueOutOfRangeException;
import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.Rating;
import com.springbite.resource_server.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public boolean isRatingValueValid(Double ratingValue) {
        return (ratingValue.compareTo(5.0) <= 0) && (ratingValue.compareTo(0.0) >= 0);
    }

    public void saveRating(Food food, String username, Double ratingValue) throws ValueOutOfRangeException {
        if (!isRatingValueValid(ratingValue)) {
            throw new ValueOutOfRangeException("Value of rating value is out of range: " + 0 + " to " + 5);
        }

        Rating rating = new Rating(
                food,
                username,
                ratingValue
        );

        ratingRepository.save(rating);
    }

    public double getAverageRating(Food food) throws ResourceNotFoundException {
        List<Rating> ratings = ratingRepository
                .findByFood(food)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found"));

        return ratings.stream()
                .mapToDouble(Rating::getRatingValue)
                .average()
                .orElse(0.0);
    }

}
