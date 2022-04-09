package MAESIK.demo.repository;

import MAESIK.demo.domain.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
    Optional<MemberGroup> findById(Long id);
    MemberGroup findByGroupId(Long groupId);
}
