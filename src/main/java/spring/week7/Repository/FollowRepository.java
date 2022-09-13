package spring.week7.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.week7.domain.Follow;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findByfollower(String email);
    int countAllByFollower(String email);
    int countAllByFollowing(String email);

}
