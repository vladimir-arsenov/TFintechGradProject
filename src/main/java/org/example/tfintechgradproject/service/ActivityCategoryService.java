package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.dto.request.CreateActivityCategoryDto;
import org.example.tfintechgradproject.dto.request.PatchActivityCategoryDto;
import org.example.tfintechgradproject.dto.response.ActivityCategoryDto;
import org.example.tfintechgradproject.mapper.ActivityCategoryMapper;
import org.example.tfintechgradproject.model.ActivityCategory;
import org.example.tfintechgradproject.repository.ActivityCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityCategoryService {

    private final ActivityCategoryRepository activityCategoryRepository;
    private final ActivityCategoryMapper activityCategoryMapper;


    public List<ActivityCategoryDto> getAll() {
        log.info("Fetching all activity categories.");

        return activityCategoryRepository.findAll().stream()
                .map(activityCategoryMapper::toActivityCategoryDto)
                .toList();
    }

    public ActivityCategoryDto get(Long id) {
        log.info("Fetching activity category with id: {}", id);

        return activityCategoryMapper.toActivityCategoryDto(getActivityCategoryById(id));
    }

    public void add(CreateActivityCategoryDto activityCategoryDto) {
        log.info("Adding new activity category: {}", activityCategoryDto);

        activityCategoryRepository.save(activityCategoryMapper.toActivityCategory(activityCategoryDto));
    }

    public void patch(Long id, PatchActivityCategoryDto activityCategoryDto) {
        log.info("Patching activity category with id: {}", id);

        var activityCategory = getActivityCategoryById(id);
        if (activityCategoryDto.getCategoryName() != null) {
            activityCategory.setName(activityCategoryDto.getCategoryName());
        }
        activityCategoryRepository.save(activityCategory);
    }

    public void delete(Long id) {
        log.info("Deleting activity category with id: {}", id);

        var activity = getActivityCategoryById(id);
        activityCategoryRepository.delete(activity);
    }

    public ActivityCategory getActivityCategoryById(Long id) {
        log.info("Fetching activity category entity with id: {}", id);

        return activityCategoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Activity category with id {} not found.", id);
                    return new EntityNotFoundException("Activity category with id %d not found".formatted(id));
                });
    }
}
