package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.dto.response.ActivityDto;
import org.example.tfintechgradproject.dto.request.CreateActivityDto;
import org.example.tfintechgradproject.dto.request.PatchActivityDto;
import org.example.tfintechgradproject.mapper.ActivityMapper;
import org.example.tfintechgradproject.model.Activity;
import org.example.tfintechgradproject.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final ActivityCategoryService activityCategoryService;

    public List<ActivityDto> getAll() {
        log.info("Fetching all activities.");

        return activityRepository.findAll().stream()
                .map(activityMapper::toActivityDto)
                .toList();
    }

    public List<ActivityDto> getActivitiesByCategory(Long categoryId) {
        log.info("Fetching activities for category with id: {}", categoryId);

        return activityRepository.findActivityByCategoryId(categoryId).stream()
                .map(activityMapper::toActivityDto)
                .toList();
    }

    public ActivityDto get(Long id) {
        log.info("Fetching activity with id: {}", id);

        return activityMapper.toActivityDto(getActivityById(id));
    }

    public void add(CreateActivityDto activityDto) {
        log.info("Adding new activity: {}", activityDto);

        activityRepository.save(activityMapper.toActivity(activityDto, activityCategoryService.getActivityCategoryById(activityDto.getCategoryId())));
    }

    public void patch(Long id, PatchActivityDto activityDto) {
        log.info("Patching activity with id: {}", id);

        var activity = getActivityById(id);
        if (activityDto.getActivityName() != null) {
            activity.setName(activityDto.getActivityName());
        }
        if (activityDto.getCategoryId() != null) {
            activity.setCategory(activityCategoryService.getActivityCategoryById(activityDto.getCategoryId()));
        }
        activityRepository.save(activity);
    }

    public void delete(Long id) {
        log.info("Deleting activity with id: {}", id);

        var activity = getActivityById(id);
        activityRepository.delete(activity);
    }

    public Activity getActivityById(Long id) {
        log.info("Fetching activity entity with id: {}", id);

        return activityRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Activity with id {} not found.", id);
                    return new EntityNotFoundException("Activity with id %s not found".formatted(id));
                });
    }
}
