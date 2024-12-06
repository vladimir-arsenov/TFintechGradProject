package org.example.tfintechgradproject.mapper;

import org.example.tfintechgradproject.dto.response.ActivityCategoryDto;
import org.example.tfintechgradproject.dto.request.CreateActivityCategoryDto;
import org.example.tfintechgradproject.model.ActivityCategory;
import org.springframework.stereotype.Component;

@Component
public class ActivityCategoryMapper {

    public ActivityCategoryDto toActivityCategoryDto(ActivityCategory activityCategory) {
        return ActivityCategoryDto.builder()
                .categoryId(activityCategory.getId())
                .categoryName(activityCategory.getName())
                .build();
    }

    public ActivityCategory toActivityCategory(CreateActivityCategoryDto activityCategoryDto) {
        return ActivityCategory.builder()
                .name(activityCategoryDto.getCategoryName())
                .build();
    }
}
