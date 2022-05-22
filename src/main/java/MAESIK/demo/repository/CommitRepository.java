package MAESIK.demo.repository;

import MAESIK.demo.domain.CommitDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommitRepository extends JpaRepository<CommitDAO, Long> {
    Optional<CommitDAO> findDistinctTopByOrderByDateDesc();
}
