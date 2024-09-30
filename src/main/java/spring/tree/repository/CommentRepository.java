package spring.tree.repository;

import spring.tree.repository.entity.CommentEntity;
import spring.tree.repository.entity.CommentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, CommentPK> {

    List<CommentEntity> findAllByOrderByCommentSeqDesc();

    List<CommentEntity> findByBoardSeqOrderByCommentSeqAsc(Long boardSeq);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COALESCE(MAX(COMMENT_SEQ), 0)+1 FROM COMMENT WHERE BOARD_SEQ = ?1",
            nativeQuery = true)
    Long getMaxCommentsSeq(Long boardSeq);
}
