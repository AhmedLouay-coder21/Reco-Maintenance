package com.Reco.backend.controller;

import com.Reco.backend.dto.request.InteractionRequest;
import com.Reco.backend.dto.request.PasswordUpdateRequest;
import com.Reco.backend.dto.request.UserRequest;
import com.Reco.backend.dto.response.RecommendationListResponse;
import com.Reco.backend.dto.response.UserResponse;
import com.Reco.backend.service.RecommendationService;
import com.Reco.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "User profiles, credentials and interaction tracking")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RecommendationService recommendationService;


    @Operation(summary = "Fetch a user by id")
    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {

        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Operation(summary = "List every registered user")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> listUsers() {

        return ResponseEntity.ok(userService.listUsers());
    }

    @Operation(summary = "Get personalized recommendations for a user")
    @GetMapping("/{userId}/recommendations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RecommendationListResponse> getRecommendations(@PathVariable Long userId) {
        return ResponseEntity.ok(recommendationService.getPersonalized(userId));
    }

    @Operation(summary = "Get the profile of the authenticated user")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> myProfile() {

        return ResponseEntity.ok(userService.myProfile());
    }

    @Operation(summary = "Track a click, view, cart add or other user interaction")
    @PostMapping("/{userId}/interactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> trackInteraction(@PathVariable Long userId,
                                                 @Valid @RequestBody InteractionRequest request) {
        recommendationService.trackInteraction(userId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update a user profile")
    @PutMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                                   @RequestBody UserRequest request) {

        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @Operation(summary = "Change a user password")
    @PatchMapping("/{userId}/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateUserPassword(@PathVariable Long userId,
                                                           @RequestBody PasswordUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUserPassword(userId, request));
    }

    @Operation(summary = "Delete a user, anonymizing personal data to preserve order history")
    @DeleteMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }

}
