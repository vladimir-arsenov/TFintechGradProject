package org.example.tfintechgradproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateActivityDto {

    @NotBlank(message = "activityName cannot be blank")
    private String activityName;

    @NotNull(message = "categoryId cannot be null")
    private Long categoryId;
}
