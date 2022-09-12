package spring.week7.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.week7.domain.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {


    List<Post> findByTitleContaining(String keyword);
    List<Post> findByContentContaining(String keyword);

    @Query(value = "SELECT p" +
            " FROM Post p" +
            " LEFT JOIN FETCH p.comments c" +
 //          " ON p.id = c.post.id" +
            " WHERE  p.id = :PostId")
    Optional<Post> findByJoinComment(@Param("PostId") Long PostId);
/*"(SELECT c From Comment c LEFT JOIN FEAT SubComment s ON c.id = s.comment.id) as c " +*/
}
