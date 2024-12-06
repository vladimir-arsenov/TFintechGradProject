package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.response.ActivityDto;
import org.example.tfintechgradproject.dto.request.CreateActivityDto;
import org.example.tfintechgradproject.dto.request.PatchActivityDto;
import org.example.tfintechgradproject.service.ActivityService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public List<ActivityDto> getAll(@RequestParam(value = "category", required = false) Long categoryId) {
        return categoryId == null
                ? activityService.getAll()
                : activityService.getActivitiesByCategory(categoryId);
    }

    @GetMapping("/{id}")
    public ActivityDto get(@PathVariable Long id) {
        return activityService.get(id);
    }

    @PostMapping
    public void add(@RequestBody CreateActivityDto activityDto) {
        activityService.add(activityDto);
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable Long id, @RequestBody PatchActivityDto activityDto) {
        activityService.patch(id, activityDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        activityService.delete(id);
    }
}
