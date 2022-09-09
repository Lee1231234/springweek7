package spring.week7.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.week7.domain.Member;
import spring.week7.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByMember(Member member);
}
