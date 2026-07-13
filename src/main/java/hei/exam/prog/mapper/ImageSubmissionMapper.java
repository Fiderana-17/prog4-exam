package hei.exam.prog.mapper;

import hei.exam.prog.dto.response.ImageSubmissionResponse;
import hei.exam.prog.entity.ImageSubmission;
import org.springframework.stereotype.Component;

@Component
public class ImageSubmissionMapper {

  public ImageSubmissionResponse toResponse(ImageSubmission entity) {
    return new ImageSubmissionResponse(entity.getId(), entity.getFileName(), entity.getEmail());
  }
}
