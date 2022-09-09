package spring.week7.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.week7.domain.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByTitleContaining(String keyword);
    List<Post> findByContentContaining(String keyword);
}
