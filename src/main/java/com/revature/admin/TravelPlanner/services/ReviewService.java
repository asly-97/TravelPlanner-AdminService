package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.ReviewDAO;
import com.revature.admin.TravelPlanner.DTOs.HotelDTO;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.ReviewNotFoundException;
import com.revature.admin.TravelPlanner.models.Hotel;
import com.revature.admin.TravelPlanner.models.Review;
import com.revature.admin.TravelPlanner.models.User;
import com.revature.admin.TravelPlanner.services.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    Logger log = LoggerFactory.getLogger(ReviewService.class);

    private ReviewDAO reviewDAO;
    private UserService userService;
    private HotelService hotelService;

    @Autowired
    public ReviewService(ReviewDAO reviewDAO, UserService userService, HotelService hotelService) {
        this.reviewDAO = reviewDAO;
        this.userService = userService;
        this.hotelService = hotelService;
    }


    public List<Review> getMostRecentReviews() throws CustomException{
        log.debug("Method 'getMostRecentReviews' invoked. ");
        List<Review> review = reviewDAO.findAll();
        if(!review.isEmpty()) {
            review = review.stream()
                    .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                    .limit(5)
                    .collect(Collectors.toList());
            log.debug("Method 'getMostRecentReviews' returning: {}", review);
            return review;
        }else {
            throw new ReviewNotFoundException("Reviews do not exist.");
        }
    }

    public HotelDTO getHighestReviewed() throws CustomException{
        List<Hotel> hotels = hotelService.findAllHotels();

        // Find the hotel with the highest average rating
        Hotel highestRatedHotel = hotels.stream()
                .max(Comparator.comparingDouble(hotel ->
                {
                    try {
                        return this.getReviewByHotelId(hotel.getHotelId()).stream()
                                .mapToDouble(Review::getStars)
                                .average()
                                .orElse(0.0);
                    } catch (ReviewNotFoundException e) {
                        return -1;
                    }
                }))
                .orElseThrow(() -> new CustomException("No hotels found"));
        return new HotelDTO(highestRatedHotel);
    }


    public List<Review> getReviewByHotelId(UUID hotelId) throws ReviewNotFoundException {
        log.debug("Method 'getReviewByHotelId' invoked with hotelId: {}", hotelId);
        List<Review> review = reviewDAO.getReviewByHotelHotelId(hotelId);
        if(review != null) {
            log.debug("Method 'getReviewByHotelId' returning: {}", review);
            return review;
        }else{
            throw new ReviewNotFoundException("Review for hotelId:"+hotelId+" does not exist.");
        }
    }

    // Delete a Review, return 1 if it exists and was successfully deleted
    public Review getReviewById(UUID reviewId) throws CustomException{
        log.debug("Method 'getReviewById' invoked with hotelId: {}", reviewId);
        Optional<Review> review = reviewDAO.findById(reviewId);
        if(review.isPresent()) {
            Review r = review.get();
            log.debug("Method 'getReviewById' returning: {}", r);
            return r;
        }
        else {
            throw new ReviewNotFoundException(reviewId);
        }
    }

}
