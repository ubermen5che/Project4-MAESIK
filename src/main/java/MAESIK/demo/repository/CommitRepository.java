package MAESIK.demo.repository;

import MAESIK.demo.domain.CommitDAO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommitRepository extends JpaRepository<CommitDAO, Long> {
    Optional<CommitDAO> findDistinctTopByMemberGroupRepoIdOrderByCommitDateDesc(Long memberGroupRepoId);
    List<CommitDAO> findByMemberGroupRepoId(Long memberGroupRepoId);
    List<CommitDAO> findByMemberGroupRepoIdAndCommitDateGreaterThanOrderByCommitDateDesc(Long memberGroupRepoId, Long dateTime);
}
