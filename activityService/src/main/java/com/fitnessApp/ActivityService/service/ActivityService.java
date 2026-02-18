package com.fitnessApp.ActivityService.service;

import com.fitnessApp.ActivityService.dto.ActivityRequest;
import com.fitnessApp.ActivityService.dto.ActivityResponse;
import com.fitnessApp.ActivityService.model.Activity;
import com.fitnessApp.ActivityService.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ModelMapper modelMapper;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse trackActivity(ActivityRequest request) {
//        Activity activity=modelMapper.map(request,Activity.class);
//        System.out.println("Before Save ID: " + activity.getId());
//        Activity savedActivity=activityRepository.save(activity);
//        System.out.println("After Save ID: " + savedActivity.getId());
//        return modelMapper.map(savedActivity, ActivityResponse.class);
        Boolean isValidUser=userValidationService.validateUser((request.getUserId()));

        if(!isValidUser){
            throw new RuntimeException("Invalid User: "+request.getUserId());
        }
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        try{
            kafkaTemplate.send(topicName,savedActivity.getUserId(),savedActivity);

        }catch(Exception e){
            e.printStackTrace();
        }

                return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }
}
