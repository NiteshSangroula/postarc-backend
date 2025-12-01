package com.postarc.postarc_backend.profile;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.postarc.postarc_backend.users.dto.UserResponse;
import com.postarc.postarc_backend.users.model.User;
import com.postarc.postarc_backend.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;

    public ProfileResponseDTO uploadProfilePicture(MultipartFile file, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (file.isEmpty()) {
            throw new RuntimeException("Empty file");
        }

        try {
            // String uploadDir = "uploads/";
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            user.setProfilePicture(fileName);
            userRepository.save(user);

            return new ProfileResponseDTO(
                    user.getId(),
                    user.getProfilePicture(),
                    user.getUsername());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file");
        }
    }
}
