package com.mitra.ai.controller;

import com.mitra.ai.dto.ActivityDTO;
import com.mitra.ai.dto.ActivityResponseDTO;
import com.mitra.ai.entity.Activity;
import com.mitra.ai.service.ActivityService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<ActivityResponseDTO>
    createActivity(
            @Valid @RequestBody ActivityDTO dto) {

        Activity activity = new Activity(
                dto.getName(),
                dto.getDescription(),
                dto.getLocation(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        Activity created =
                activityService.createActivity(
                        activity,
                        dto.getItineraryId());

        return new ResponseEntity<>(
                toDTO(created),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponseDTO>>
    getAllActivities() {

        return ResponseEntity.ok(
                activityService.getAllActivities()
                        .stream()
                        .map(this::toDTO)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponseDTO>
    getActivityById(@PathVariable Long id) {

        return activityService.getActivityById(id)
                .map(activity ->
                        ResponseEntity.ok(toDTO(activity)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/itinerary/{itineraryId}")
    public ResponseEntity<List<ActivityResponseDTO>>
    getByItinerary(
            @PathVariable Long itineraryId) {

        return ResponseEntity.ok(
                activityService
                        .getActivitiesByItinerary(itineraryId)
                        .stream()
                        .map(this::toDTO)
                        .toList());
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<ActivityResponseDTO>>
    getByLocation(@PathVariable String location) {

        return ResponseEntity.ok(
                activityService
                        .getActivitiesByLocation(location)
                        .stream()
                        .map(this::toDTO)
                        .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponseDTO>
    updateActivity(
            @PathVariable Long id,
            @Valid @RequestBody ActivityDTO dto) {

        Activity updated = new Activity(
                dto.getName(),
                dto.getDescription(),
                dto.getLocation(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        Activity result =
                activityService.updateActivity(
                        id,
                        updated,
                        dto.getItineraryId());

        return ResponseEntity.ok(toDTO(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(
            @PathVariable Long id) {

        activityService.deleteActivity(id);

        return ResponseEntity.noContent().build();
    }

    private ActivityResponseDTO toDTO(
            Activity activity) {

        return new ActivityResponseDTO(
                activity.getId(),
                activity.getItinerary().getId(),
                activity.getName(),
                activity.getDescription(),
                activity.getLocation(),
                activity.getStartTime(),
                activity.getEndTime(),
                activity.getCreatedAt(),
                activity.getUpdatedAt()
        );
    }
}