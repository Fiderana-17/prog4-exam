package hei.exam.prog;

import static org.assertj.core.api.Assertions.assertThat;

import hei.exam.prog.conf.FacadeIT;
import hei.exam.prog.dto.response.ImageSubmissionResponse;
import hei.exam.prog.repository.ImageSubmissionRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class ImageSubmissionIT extends FacadeIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private ImageSubmissionRepository imageSubmissionRepository;

  @Test
  void postImageSubmissionWithPngShouldReturn200() {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("email", "test@example.com");
    body.add(
        "file",
        new org.springframework.mock.web.MockMultipartFile(
            "file", "photo.png", "image/png", "fake png content".getBytes()));

    ResponseEntity<ImageSubmissionResponse> response =
        restTemplate.exchange(
            "/image-submissions",
            HttpMethod.POST,
            new HttpEntity<>(
                body,
                new HttpHeaders() {
                  {
                    setContentType(MediaType.MULTIPART_FORM_DATA);
                  }
                }),
            ImageSubmissionResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().email()).isEqualTo("test@example.com");
    assertThat(response.getBody().fileName()).isEqualTo("photo.png");
    assertThat(response.getBody().id()).isNotNull();
  }

  @Test
  void postImageSubmissionWithJpegShouldReturn200() {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("email", "user@test.com");
    body.add(
        "file",
        new org.springframework.mock.web.MockMultipartFile(
            "file", "photo.jpg", "image/jpeg", "fake jpeg content".getBytes()));

    ResponseEntity<ImageSubmissionResponse> response =
        restTemplate.exchange(
            "/image-submissions",
            HttpMethod.POST,
            new HttpEntity<>(
                body,
                new HttpHeaders() {
                  {
                    setContentType(MediaType.MULTIPART_FORM_DATA);
                  }
                }),
            ImageSubmissionResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().email()).isEqualTo("user@test.com");
  }

  @Test
  void postImageSubmissionWithInvalidEmailShouldReturn400() {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("email", "not-an-email");
    body.add(
        "file",
        new org.springframework.mock.web.MockMultipartFile(
            "file", "photo.png", "image/png", "fake png content".getBytes()));

    ResponseEntity<String> response =
        restTemplate.exchange(
            "/image-submissions",
            HttpMethod.POST,
            new HttpEntity<>(
                body,
                new HttpHeaders() {
                  {
                    setContentType(MediaType.MULTIPART_FORM_DATA);
                  }
                }),
            String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void postImageSubmissionWithWrongContentTypeShouldReturn400() {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("email", "test@example.com");
    body.add(
        "file",
        new org.springframework.mock.web.MockMultipartFile(
            "file", "doc.pdf", "application/pdf", "fake pdf content".getBytes()));

    ResponseEntity<String> response =
        restTemplate.exchange(
            "/image-submissions",
            HttpMethod.POST,
            new HttpEntity<>(
                body,
                new HttpHeaders() {
                  {
                    setContentType(MediaType.MULTIPART_FORM_DATA);
                  }
                }),
            String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void getShouldReturnAllSubmissions() {
    imageSubmissionRepository.deleteAll();

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("email", "a@b.com");
    body.add(
        "file",
        new org.springframework.mock.web.MockMultipartFile(
            "file", "img.png", "image/png", "content".getBytes()));
    restTemplate.exchange(
        "/image-submissions",
        HttpMethod.POST,
        new HttpEntity<>(
            body,
            new HttpHeaders() {
              {
                setContentType(MediaType.MULTIPART_FORM_DATA);
              }
            }),
        ImageSubmissionResponse.class);

    ResponseEntity<List> response = restTemplate.getForEntity("/image-submissions", List.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotEmpty();
  }

  @Test
  void postThenGetShouldPersist() {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("email", "persist@test.com");
    body.add(
        "file",
        new org.springframework.mock.web.MockMultipartFile(
            "file", "test.png", "image/png", "data".getBytes()));

    ResponseEntity<ImageSubmissionResponse> postResponse =
        restTemplate.exchange(
            "/image-submissions",
            HttpMethod.POST,
            new HttpEntity<>(
                body,
                new HttpHeaders() {
                  {
                    setContentType(MediaType.MULTIPART_FORM_DATA);
                  }
                }),
            ImageSubmissionResponse.class);

    UUID id = postResponse.getBody().id();

    assertThat(imageSubmissionRepository.findById(id)).isPresent();
    assertThat(imageSubmissionRepository.findById(id).get().getEmail())
        .isEqualTo("persist@test.com");
  }
}
