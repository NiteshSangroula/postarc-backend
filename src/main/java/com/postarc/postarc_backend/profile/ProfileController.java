package com.postarc.postarc_backend.profile;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.postarc.postarc_backend.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileService profileService;

  @PostMapping("/me/profile-picture")
  public ResponseEntity<ApiResponse<ProfileResponseDTO>> uploadProfilePicture(
      @RequestParam("file") MultipartFile file,
      Principal principal) {
    ProfileResponseDTO profile = profileService.uploadProfilePicture(file, principal.getName());
    return ResponseEntity.ok(ApiResponse.success(profile, "Profile picture updated successfully"));
  }
}
