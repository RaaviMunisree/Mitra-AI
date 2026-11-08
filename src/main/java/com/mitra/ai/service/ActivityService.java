package com.mitra.ai.service;

import com.mitra.ai.entity.Activity;
import com.mitra.ai.entity.Itinerary;
import com.mitra.ai.repository.ActivityRepository;
import com.mitra.ai.repository.ItineraryRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ItineraryRepository itineraryRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            ItineraryRepository itineraryRepository) {

        this.activityRepository = activityRepository;
        this.itineraryRepository = itineraryRepository;
    }

    public Activity createActivity(
            Activity activity,
            Long itineraryId) {

        Itinerary itinerary =
                itineraryRepository.findById(itineraryId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Itinerary not found"));

        activity.setItinerary(itinerary);

        return activityRepository.save(activity);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    public List<Activity> getActivitiesByItinerary(
            Long itineraryId) {

        if (!itineraryRepository.existsById(itineraryId)) {
            throw new IllegalArgumentException(
                    "Itinerary not found");
        }

        return activityRepository
                .findByItineraryId(itineraryId);
    }

    public List<Activity> getActivitiesByLocation(
            String location) {

        return activityRepository
                .findByLocationIgnoreCase(location);
    }

    public Activity updateActivity(
            Long id,
            Activity updated,
            Long itineraryId) {

        Activity existing =
                activityRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Activity not found"));

        Itinerary itinerary =
                itineraryRepository.findById(itineraryId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Itinerary not found"));

        existing.setItinerary(itinerary);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setLocation(updated.getLocation());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());

        return activityRepository.save(existing);
    }

    public void deleteActivity(Long id) {

        if (!activityRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Activity not found");
        }

        activityRepository.deleteById(id);
    }
}