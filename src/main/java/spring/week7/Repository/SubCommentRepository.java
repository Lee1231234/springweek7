package spring.week7.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.week7.domain.SubComment;

public interface SubCommentRepository extends JpaRepository<SubComment,Long> {
}
