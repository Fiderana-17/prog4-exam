package hei.exam.prog.service;

import hei.exam.prog.entity.ImageSubmission;
import hei.exam.prog.file.bucket.BucketComponent;
import hei.exam.prog.mail.Email;
import hei.exam.prog.mail.Mailer;
import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageSubmissionAsyncService {

  private final BucketComponent bucketComponent;
  private final Mailer mailer;

  @Async
  public void uploadAndNotify(ImageSubmission entity, byte[] fileContent, String contentType) {
    String id = entity.getId().toString();
    String email = entity.getEmail();
    String fileName = entity.getFileName();

    try {
      String bucketKey = id + "-" + fileName;
      File tempFile = File.createTempFile("upload-", "-" + fileName);
      Files.write(tempFile.toPath(), fileContent);

      bucketComponent.upload(tempFile, bucketKey);
      String s3Url = bucketComponent.presign(bucketKey, Duration.ofDays(7)).toString();

      Email message =
          new Email(
              new InternetAddress(email),
              List.of(),
              List.of(),
              "Votre image a été traitée",
              "<p>Voici le lien de votre image : <a href=\"" + s3Url + "\">" + s3Url + "</a></p>",
              List.of());
      mailer.accept(message);

      tempFile.delete();
    } catch (Exception e) {
      log.error("Échec de l'upload S3 ou de l'envoi d'email pour la soumission {}", id, e);
    }
  }
}
