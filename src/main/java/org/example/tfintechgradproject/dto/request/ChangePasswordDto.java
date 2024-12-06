package org.example.tfintechgradproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDto {

    @NotBlank(message = "currentPassword cannot be blank")
    private String currentPassword;

    @NotBlank(message = "newPassword cannot be blank")
    @Size(min = 8, message = "newPassword must be at least 8 characters long")
    private String newPassword;

    @NotBlank(message = "confirmationPassword cannot be blank")
    private String confirmationPassword;
}
