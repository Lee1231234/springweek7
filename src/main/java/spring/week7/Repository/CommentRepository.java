package spring.week7.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.week7.domain.Comment;
import spring.week7.domain.SubComment;

public interface CommentRepository extends JpaRepository<Comment,Long> {


}
