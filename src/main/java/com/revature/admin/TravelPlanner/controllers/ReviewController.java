package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.DTOs.HotelDTO;
import com.revature.admin.TravelPlanner.models.Review;
import com.revature.admin.TravelPlanner.services.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@CrossOrigin
public class ReviewController {
    Logger log = LoggerFactory.getLogger(ReviewController.class);
    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping("/hotel/best")
    public ResponseEntity<HotelDTO> getHighestRated(){
        log.debug("Endpoint GET ./reviews/hotel/best reached");
        try{
            return ResponseEntity.ok(reviewService.getHighestReviewed());
        }catch(Exception e){
            log.warn("Exception was thrown", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Review>> getLatestReviewed(){
        log.debug("Endpoint GET ./reviews/latest reached");
        try{
            return ResponseEntity.ok(reviewService.getMostRecentReviews());
        }catch(Exception e){
            log.warn("Exception was thrown", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Review>> getAllFavoriteByHotel(@PathVariable UUID hotelId){
        log.debug("Endpoint GET ./favorite/hotel={}",hotelId);
        try{
            return ResponseEntity.ok(reviewService.getReviewByHotelId(hotelId));
        }catch(Exception e){
            log.warn("Exception was thrown", e);
            return ResponseEntity.status(404).body(null);
        }
    }
}