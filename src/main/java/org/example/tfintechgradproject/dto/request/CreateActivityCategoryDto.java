package org.example.tfintechgradproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateActivityCategoryDto {

    @NotBlank(message = "categoryName cannot be blank")
    private String categoryName;
}