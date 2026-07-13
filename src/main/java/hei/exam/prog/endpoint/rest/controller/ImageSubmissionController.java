package hei.exam.prog.endpoint.rest.controller;

import hei.exam.prog.dto.response.ImageSubmissionResponse;
import hei.exam.prog.entity.ImageSubmission;
import hei.exam.prog.mapper.ImageSubmissionMapper;
import hei.exam.prog.repository.ImageSubmissionRepository;
import hei.exam.prog.service.ImageSubmissionAsyncService;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageSubmissionController {

  private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/png", "image/jpeg");

  private final ImageSubmissionRepository imageSubmissionRepository;
  private final ImageSubmissionMapper imageSubmissionMapper;
  private final ImageSubmissionAsyncService imageSubmissionAsyncService;

  @PostMapping(value = "/image-submissions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ImageSubmissionResponse create(
      @RequestParam String email, @RequestParam MultipartFile file) throws IOException {
    if (email == null || email.isBlank() || !email.contains("@")) {
      throw new IllegalArgumentException("Email invalide : " + email);
    }

    String contentType = file.getContentType();
    if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
      throw new IllegalArgumentException("Le fichier doit être un PNG ou un JPG");
    }

    ImageSubmission entity =
        ImageSubmission.builder()
            .id(UUID.randomUUID())
            .fileName(file.getOriginalFilename())
            .email(email)
            .createdAt(Instant.now())
            .build();
    imageSubmissionRepository.save(entity);

    imageSubmissionAsyncService.uploadAndNotify(entity, file.getBytes(), contentType);

    return imageSubmissionMapper.toResponse(entity);
  }

  @GetMapping("/image-submissions")
  public List<ImageSubmissionResponse> findAll() {
    return imageSubmissionRepository.findAll().stream()
        .map(imageSubmissionMapper::toResponse)
        .toList();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
  }
}
