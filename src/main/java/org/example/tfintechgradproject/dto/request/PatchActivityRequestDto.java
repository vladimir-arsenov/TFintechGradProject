package org.example.tfintechgradproject.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchActivityRequestDto {

    private Long activityId;

    private String location;

    private String comment;

    @Positive(message = "participantsRequired must be greater than 0")
    private Integer participantsRequired;

    private String status;

    @Future(message = "joinDeadline must be in the future")
    private LocalDateTime joinDeadline;

    @Future(message = "activityStart must be in the future")
    private LocalDateTime activityStart;
}