package spring.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.tree.repository.entity.ReservationEntity;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByTourSeq(Long tourSeq);

    List<ReservationEntity> findByUserId(String userId);

    boolean existsByTourSeqAndUserId(Long tourSeq, String userId);

    int countByTourSeq(Long tourSeq);
}
