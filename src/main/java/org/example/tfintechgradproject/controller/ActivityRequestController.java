package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.response.ActivityRequestDto;
import org.example.tfintechgradproject.dto.response.ActivityRequestPreviewDto;
import org.example.tfintechgradproject.dto.request.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.request.PatchActivityRequestDto;
import org.example.tfintechgradproject.security.auth.UserPrincipal;
import org.example.tfintechgradproject.service.ActivityRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("api/v1/activity-request")
@RequiredArgsConstructor
public class ActivityRequestController {

    private final ActivityRequestService activityRequestService;

    @GetMapping
    public List<ActivityRequestPreviewDto> getClosestActivityRequests(
            @RequestParam("activity") Long activityId,
            @RequestParam("location") String location,
            @RequestParam(value = "radius", defaultValue = "3000") Double radius
    ) {
        return activityRequestService.getClosestActivityRequests(activityId, location, radius);
    }

    @GetMapping("/{id}/preview")
    public ActivityRequestPreviewDto getPreview(@PathVariable Long id) {
        return activityRequestService.getPreview(id);
    }

    @PreAuthorize("@activityRequestService.isOwner(authentication, #id)")
    @GetMapping("/{id}")
    public ActivityRequestDto get(@PathVariable Long id) {
        return activityRequestService.get(id);
    }

    @PreAuthorize("@activityRequestService.isOwner(authentication, #id)")
    @PatchMapping("/{id}")
    public void patch(@PathVariable Long id, @RequestBody PatchActivityRequestDto activityRequestDto) {
        activityRequestService.patch(id, activityRequestDto);
    }

    @PostMapping("/{id}/join")
    public void join(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal authenticatedUser) {
        activityRequestService.join(id, authenticatedUser);
    }

    @PostMapping
    public void add(@RequestBody CreateActivityRequestDto activityRequestDto, @AuthenticationPrincipal UserPrincipal authenticatedUser) {
        activityRequestService.add(activityRequestDto, authenticatedUser);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        activityRequestService.delete(id);
    }
}
