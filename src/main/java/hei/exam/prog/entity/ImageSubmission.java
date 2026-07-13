package hei.exam.prog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_submission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageSubmission {
  @Id private UUID id;
  private String fileName;
  private String email;
  private Instant createdAt;
}
