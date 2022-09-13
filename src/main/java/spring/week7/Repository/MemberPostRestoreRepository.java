package spring.week7.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.week7.domain.Member;
import spring.week7.domain.MemberPostRestore;
import spring.week7.domain.Post;

import java.util.Optional;

public interface MemberPostRestoreRepository extends JpaRepository<MemberPostRestore,Long> {
    Optional<MemberPostRestore> findByPostAndMember(Post post, Member member);
}
