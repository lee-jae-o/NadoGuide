package spring.tree.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.tree.repository.entity.BoardEntity;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findAllByOrderByBoardSeqDesc();

    BoardEntity findByBoardSeq(Long boardSeq);


    Page<BoardEntity> findAllByOrderByBoardSeqDesc(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BOARD SET READ_CNT = COALESCE(READ_CNT, 0) + 1 WHERE board_seq = ?1", nativeQuery = true)
    int updateReadCnt(Long boardSeq);

}
