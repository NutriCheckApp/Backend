package com.nutricheck.backend.controller;

import com.nutricheck.backend.dto.FoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    // todo: inject foodService

    @GetMapping
    public ResponseEntity<List<FoodResponse>> searchFoods(@RequestParam(value = "search", required = false) String query) {
        // todo: call foodservice
        // todo: use Pages
         return ResponseEntity.ok(List.of(new FoodResponse()));
    }

    @GetMapping("/{food_id}")
    public ResponseEntity<FoodResponse> getFoodById(@PathVariable Long food_id) {
        // todo: call service
        return ResponseEntity.ok(new FoodResponse());
    }

    // todo: Update food?

    // todo: delete food?
}
