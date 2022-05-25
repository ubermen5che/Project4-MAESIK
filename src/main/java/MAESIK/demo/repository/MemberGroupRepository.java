package MAESIK.demo.repository;

import MAESIK.demo.domain.Member;
import MAESIK.demo.domain.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
    Optional<MemberGroup> findById(Long id);
    List<MemberGroup> findByGroup_Id(Long groupId);
}
