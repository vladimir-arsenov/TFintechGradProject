package org.example.tfintechgradproject.mapper;

import org.example.tfintechgradproject.dto.response.ActivityDto;
import org.example.tfintechgradproject.dto.request.CreateActivityDto;
import org.example.tfintechgradproject.model.Activity;
import org.example.tfintechgradproject.model.ActivityCategory;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
    public ActivityDto toActivityDto(Activity activity) {
        return ActivityDto.builder()
                .activityId(activity.getId())
                .activityName(activity.getName())
                .categoryName(activity.getCategory().getName())
                .categoryId(activity.getCategory().getId())
                .build();
    }

    public Activity toActivity(CreateActivityDto activityDto, ActivityCategory category) {
        return Activity.builder()
                .name(activityDto.getActivityName())
                .category(category)
                .build();
    }
}
