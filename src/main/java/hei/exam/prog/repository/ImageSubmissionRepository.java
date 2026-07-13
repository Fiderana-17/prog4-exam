package hei.exam.prog.repository;

import hei.exam.prog.entity.ImageSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageSubmissionRepository extends JpaRepository<ImageSubmission, UUID> {
}