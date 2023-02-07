package mza.thy.repository;

import mza.thy.domain.Cure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface CureRepository extends JpaRepository<Cure, Long> {

    @Query(value = "SELECT C FROM Cure C " +
            "WHERE C.medicine like :medicine")
    Stream<Cure> findAllByMedicineLike(String medicine);

    @Query(value = "SELECT C FROM Cure C " +
            "WHERE C.description like :description")
    Stream<Cure> findAllByDescriptionLike(String description);

    @Query(value = "SELECT C FROM Cure C " +
            "WHERE C.illnessId =:illnessId")
    Stream<Cure> findAllByIllnessId(Long illnessId);
}
