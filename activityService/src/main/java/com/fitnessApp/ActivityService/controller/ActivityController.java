package com.fitnessApp.ActivityService.controller;

import com.fitnessApp.ActivityService.dto.ActivityRequest;
import com.fitnessApp.ActivityService.dto.ActivityResponse;
import com.fitnessApp.ActivityService.model.Activity;
import com.fitnessApp.ActivityService.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request){
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

}
