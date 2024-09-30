package spring.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.tree.repository.entity.FavoriteTourEntity;

import java.util.List;

@Repository
public interface FavoriteTourRepository extends JpaRepository<FavoriteTourEntity, Long> {
    List<FavoriteTourEntity> findByUserId(String userId);
    boolean existsByUserIdAndTourSeq(String userId, Long tourSeq);
    void deleteByUserIdAndTourSeq(String userId, Long tourSeq);
}