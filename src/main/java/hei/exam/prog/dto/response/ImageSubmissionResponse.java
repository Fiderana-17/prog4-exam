package hei.exam.prog.dto.response;

import java.util.UUID;

public record ImageSubmissionResponse(
        UUID id,
        String fileName,
        String email
) {
}
