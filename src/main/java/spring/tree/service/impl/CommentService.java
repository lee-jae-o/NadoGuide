package spring.tree.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import spring.tree.dto.CommentDTO;
import spring.tree.repository.CommentRepository;
import spring.tree.repository.entity.CommentEntity;
import spring.tree.repository.entity.CommentPK;
import spring.tree.service.ICommentService;
import spring.tree.util.CmmUtil;
import spring.tree.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;


    @Override
    public List<CommentDTO> getCommentList(CommentDTO pDTO) {

        log.info(this.getClass().getName() + ".getCommentList Start!");

        Long boardSeq = pDTO.boardSeq();

        log.info("boardSeq : " + boardSeq);

        List<CommentEntity> rList = commentRepository.findByBoardSeqOrderByCommentSeqAsc(boardSeq);

        List<CommentDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<>() {
                });

        log.info(this.getClass().getName() + ".getCommentList End!");

        return nList;
    }

    @Override
    public int updateComment(CommentDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateComment Start!");

        Long boardSeq = pDTO.boardSeq();
        Long commentSeq = pDTO.commentSeq();

        CommentPK commentPK = CommentPK.builder()
                .boardSeq(boardSeq)
                .commentSeq(commentSeq)
                .build();

        int res = 0;

        Optional<CommentEntity> rEntity = commentRepository.findById(commentPK);

        if(rEntity.isPresent()){

            String userId = pDTO.userId();
            String comment = pDTO.comment();
            String chgDt = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss");
            String regDt = rEntity.get().getRegDt();

            log.info("boardSeq : " + boardSeq);
            log.info("commentSeq : " + commentSeq);
            log.info("userId : " + userId);
            log.info("comment : " + comment);
            log.info("chgDt : " + chgDt);

            // 댓글 내용 DB에 저장 Update
            CommentEntity pEntity = CommentEntity.builder()
                    .boardSeq(boardSeq)
                    .commentSeq(commentSeq)
                    .userId(userId)
                    .comment(comment)
                    .regDt(regDt)
                    .chgDt(chgDt)
                    .build();

            commentRepository.save(pEntity);

            res = 1;
        }

        log.info(this.getClass().getName() + ".updateComment End!");

        return res;
    }


    @Override
    public void deleteComment(CommentDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteComment Start!");

        Long boardSeq = pDTO.boardSeq();
        Long commentSeq = pDTO.commentSeq();

        log.info("boardSeq : " + boardSeq);
        log.info("commentSeq : " + commentSeq);

        CommentPK commentPK = CommentPK.builder()
                .boardSeq(boardSeq)
                .commentSeq(commentSeq)
                .build();

        // 데이터 삭제하기
        commentRepository.deleteById(commentPK);

        log.info(this.getClass().getName() + ".deleteComment End!");

    }

    @Override
    public void insertComment(CommentDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertComment Start!");

        String userId = CmmUtil.nvl(pDTO.userId());
        String comment = CmmUtil.nvl(pDTO.comment());
        Long boardSeq = pDTO.boardSeq();
        Long commentSeq = commentRepository.getMaxCommentsSeq(boardSeq);

        log.info("userId : " + userId);
        log.info("comment : " + comment);
        log.info("boardSeq : " + boardSeq);
        log.info("commentSeq : " + commentSeq);

        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        CommentEntity pEntity = CommentEntity.builder()
                .boardSeq(boardSeq)
                .commentSeq(commentSeq)
                .userId(userId)
                .comment(comment)
                .regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        // 공지사항 저장하기
        commentRepository.save(pEntity);

        log.info(this.getClass().getName() + ".insertComment End!");

    }

}