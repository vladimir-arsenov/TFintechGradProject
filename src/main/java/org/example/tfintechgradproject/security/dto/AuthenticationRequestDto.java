package org.example.tfintechgradproject.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequestDto {

  @Email(message = "Invalid email format")
  @NotBlank(message = "email cannot be blank")
  private String email;

  @NotBlank(message = "password cannot be blank")
  private String password;

  private boolean rememberMe;
}