package hei.exam.prog.repository;

import hei.exam.prog.entity.ImageSubmission;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageSubmissionRepository extends JpaRepository<ImageSubmission, UUID> {}
