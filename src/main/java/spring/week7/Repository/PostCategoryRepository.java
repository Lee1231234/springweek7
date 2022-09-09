package spring.week7.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.week7.domain.PostCategory;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    PostCategory findByName(String name);
}

