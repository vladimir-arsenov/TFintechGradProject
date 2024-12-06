package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.response.ActivityCategoryDto;
import org.example.tfintechgradproject.dto.request.CreateActivityCategoryDto;
import org.example.tfintechgradproject.dto.request.PatchActivityCategoryDto;
import org.example.tfintechgradproject.service.ActivityCategoryService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/activity-category")
@RequiredArgsConstructor
public class ActivityCategoryController {

    private final ActivityCategoryService activityCategoryService;

    @GetMapping
    public List<ActivityCategoryDto> getAll() {
        return activityCategoryService.getAll();
    }

    @GetMapping("/{id}")
    public ActivityCategoryDto get(@PathVariable Long id) {
        return activityCategoryService.get(id);
    }

    @PostMapping
    public void add(@RequestBody CreateActivityCategoryDto activityCategoryDto) {
        activityCategoryService.add(activityCategoryDto);
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable Long id, @RequestBody PatchActivityCategoryDto activityCategoryDto) {
        activityCategoryService.patch(id, activityCategoryDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        activityCategoryService.delete(id);
    }
}
