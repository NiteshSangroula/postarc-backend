package com.postarc.postarc_backend.files;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/uploads")
public class FileController {
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws Exception {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Path path = Paths.get(uploadDir + filename);
        Resource resource = new UrlResource(path.toUri());

        System.out.println("Fetching file from: " + path.toString());
        System.out.println("Looking for file at: " + path.toAbsolutePath());

        if (!resource.exists()) {
            throw new RuntimeException("File not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // simple assumption
                .body(resource);

    }

}
