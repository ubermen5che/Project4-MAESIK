package MAESIK.demo.repository;

import MAESIK.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthId(String id);
    Optional<Member> findById(Long id);
    //@Query("select m from Member m join MemberGroup mg where m.memberId = mg.member")
    List<Member> findAll();
    Optional<Member> findByGithubId(String id);
}
