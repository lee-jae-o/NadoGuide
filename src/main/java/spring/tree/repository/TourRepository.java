package spring.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.tree.repository.entity.TourEntity;

import java.util.List;

public interface TourRepository extends JpaRepository<TourEntity, Long> {

    List<TourEntity> findAllByOrderByTourSeqDesc();

    TourEntity findByTourSeq(Long TourSeq);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE TOUR_INFO SET READ_CNT = COALESCE(READ_CNT, 0) + 1 WHERE tour_seq = ?1", nativeQuery = true)
    int updateReadCnt(Long tourSeq);

}
