package org.example.tfintechgradproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityDto {

    private String activityName;

    private Long categoryId;

    private String categoryName;
}
