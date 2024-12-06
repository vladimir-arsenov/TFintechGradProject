package org.example.tfintechgradproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityDto {

    private Long activityId;

    private String activityName;

    private Long categoryId;

    private String categoryName;
}
