package MAESIK.demo.repository;

import MAESIK.demo.domain.Group;
import MAESIK.demo.domain.dto.GroupResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findById(Long id);
    //Optional<Group> findByMasterId(Long id);
    List<Group> findAll();
    @Query("SELECT new MAESIK.demo.domain.dto.GroupResponseDTO(g.id, g.groupName, g.groupExp, g.groupTier, g.groupMasterId)" +
    " from Member m" +
            " join m.memberGroupList mg" +
            " join mg.group g" +
            " where m.oauthId like %:memberId%")
    List<GroupResponseDTO> findGroupDTOs(@Param("memberId") String memberId);
    void deleteById(Long id);
}
