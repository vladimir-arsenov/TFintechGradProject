package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.request.ChangePasswordDto;
import org.example.tfintechgradproject.security.auth.UserPrincipal;
import org.example.tfintechgradproject.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/password")
    public void changePassword(@RequestBody ChangePasswordDto request, @AuthenticationPrincipal UserPrincipal authenticatedUser) {
        userService.changePassword(request, authenticatedUser);
    }
}
