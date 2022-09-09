package spring.week7.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.week7.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
}
