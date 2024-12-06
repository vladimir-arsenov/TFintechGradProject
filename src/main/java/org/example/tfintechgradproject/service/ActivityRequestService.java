package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.client.YandexMapsClient;
import org.example.tfintechgradproject.dto.response.ActivityRequestDto;
import org.example.tfintechgradproject.dto.response.ActivityRequestPreviewDto;
import org.example.tfintechgradproject.dto.request.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.request.PatchActivityRequestDto;
import org.example.tfintechgradproject.exception.exceptions.CannotJoinActivityRequestException;
import org.example.tfintechgradproject.mapper.ActivityRequestMapper;
import org.example.tfintechgradproject.model.ActivityRequest;
import org.example.tfintechgradproject.model.ActivityRequestStatus;
import org.example.tfintechgradproject.repository.ActivityRequestRepository;
import org.example.tfintechgradproject.security.auth.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityRequestService {

    private final ActivityRequestRepository activityRequestRepository;
    private final ActivityRequestMapper activityRequestMapper;
    private final YandexMapsClient yandexMapsClient;
    private final ActivityService activityService;
    private final UserService userService;

    public List<ActivityRequestPreviewDto> getClosestActivityRequests(Long activityId, String location, Double radius) {
        log.info("Fetching closest activity requests for activityId: {}, location: {}, radius: {}", activityId, location, radius);

        var activity = activityService.getActivityById(activityId);
        var locationInfo =  yandexMapsClient.getLocationInfo(location);

        return activityRequestRepository.findActiveClosestActivityRequests(activity.getId(), locationInfo.getCoordinates().toString(), radius)
                .stream()
                .map(activityRequestMapper::toActivityRequestPreviewDto)
                .toList();
    }

    public void join(Long id, UserPrincipal authenticatedUser) {
        log.info("User [{}] is attempting to join activity request with id: {}", authenticatedUser.getUsername(), id);

        var user = userService.findByEmail(authenticatedUser.getUsername());
        var activityRequest = getActivityRequestById(id);
        var participants = activityRequest.getParticipants();

        if (!activityRequest.getStatus().equals(ActivityRequestStatus.ACTIVE) || activityRequest.getParticipantsRequired() - participants.size() < 1) {
            log.warn("Cannot join activity request. Status: {}, Remaining spots: {}", activityRequest.getStatus(), activityRequest.getParticipantsRequired() - participants.size());

            throw new CannotJoinActivityRequestException("Activity request is not active anymore or all spots are filled");
        }

        participants.add(user);
        activityRequest.setParticipants(participants);
        activityRequestRepository.save(activityRequest);
    }

    public ActivityRequestDto get(Long id) {
        log.info("Fetching activity request with id: {}", id);

        return activityRequestMapper.toActivityRequestDto(getActivityRequestById(id));
    }

    public ActivityRequestPreviewDto getPreview(Long id) {
        log.info("Fetching preview for activity request with id: {}", id);

        return activityRequestMapper.toActivityRequestPreviewDto(getActivityRequestById(id));
    }

    public void add(CreateActivityRequestDto activityRequestDto, UserPrincipal authenticatedUser) {
        log.info("Adding new activity request by user [{}]: {}", authenticatedUser.getUsername(), activityRequestDto);

        var locationInfo = yandexMapsClient.getLocationInfo(activityRequestDto.getLocation());
        var activity = activityService.getActivityById(activityRequestDto.getActivityId());
        var creator = userService.findByEmail(authenticatedUser.getUsername());
        activityRequestRepository.save(activityRequestMapper.toActivityRequest(activityRequestDto, activity, locationInfo, creator));
    }

    public void patch(Long id, PatchActivityRequestDto activityRequestDto) {
        log.info("Patching activity request with id: {}", id);

        var activityRequest = getActivityRequestById(id);
        if (activityRequestDto.getStatus() != null) {
            activityRequest.setStatus(ActivityRequestStatus.valueOf(activityRequestDto.getStatus()));
        }
        if (activityRequestDto.getActivityId() != null) {
            activityRequest.setActivity(activityService.getActivityById(activityRequestDto.getActivityId()));
        }
        if (activityRequestDto.getComment() != null) {
            activityRequest.setComment(activityRequestDto.getComment());
        }
        if (activityRequestDto.getLocation() != null) {
            var locationInfo = yandexMapsClient.getLocationInfo(activityRequestDto.getLocation());
            activityRequest.setAddress(locationInfo.getAddress());
            activityRequest.setCoordinates(locationInfo.getCoordinates());
        }
        if (activityRequestDto.getParticipantsRequired() != null) {
            activityRequest.setParticipantsRequired(activityRequestDto.getParticipantsRequired());
        }
        if (activityRequestDto.getJoinDeadline() != null) {
            activityRequest.setJoinDeadline(activityRequestDto.getJoinDeadline());
        }
        if (activityRequestDto.getActivityStart() != null) {
            activityRequest.setActivityStart(activityRequestDto.getActivityStart());
        }
        activityRequestRepository.save(activityRequest);
    }

    public void delete(Long id) {
        log.info("Deleting activity request with id: {}", id);

        var activityRequest = getActivityRequestById(id);
        activityRequestRepository.delete(activityRequest);
    }

    public boolean isOwner(Authentication authentication, Long id) {
        log.info("Checking if user [{}] has permission to access activity request with id: {}", authentication.getName(), id);

        return getActivityRequestById(id).getCreator().getEmail().equals(authentication.getName());
    }

    private ActivityRequest getActivityRequestById(Long id) {
        log.info("Fetching activity request entity with id: {}", id);

        return activityRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Activity request with id {} not found.", id);
                    return new EntityNotFoundException("Activity request with id %d not found".formatted(id));
                });
    }
}
