package org.example.tfintechgradproject.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateActivityRequestDto {

    @NotNull(message = "activityId cannot be null")
    private Long activityId;

    @NotBlank(message = "location cannot be blank")
    private String location;

    @Future(message = "joinDeadline must be in the future")
    private LocalDateTime joinDeadline;

    @Future(message = "activityStart must be in the future")
    private LocalDateTime activityStart;

    private String comment;

    @NotNull(message = "participantsRequired cannot be null")
    @Positive(message = "participantsRequired must be greater than 0")
    private Integer participantsRequired;
}