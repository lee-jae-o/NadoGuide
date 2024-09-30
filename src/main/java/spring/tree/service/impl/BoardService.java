package spring.tree.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.tree.dto.BoardDTO;
import spring.tree.dto.UserInfoDTO;
import spring.tree.repository.BoardRepository;
import spring.tree.repository.entity.BoardEntity;
import spring.tree.service.IBoardService;
import spring.tree.util.CmmUtil;
import spring.tree.util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService implements IBoardService {

    private final BoardRepository boardRepository;
    private final UserInfoService userInfoService;


    @Override
    public List<BoardDTO> getBoardList() {
        log.info(this.getClass().getName() + ".getBoardList Start!");

        List<BoardEntity> rList = boardRepository.findAllByOrderByBoardSeqDesc();

        List<BoardDTO> nList = new ObjectMapper().convertValue(rList, new TypeReference<>() {
        });

        List<BoardDTO> updatedList = new ArrayList<>();
        for (BoardDTO board : nList) {
            try {
                UserInfoDTO userInfo = userInfoService.getUserIdExists(UserInfoDTO.builder().userId(board.userId()).build());
                if ("Y".equals(userInfo.existsYn())) {
                    BoardDTO updatedBoard = BoardDTO.builder()
                            .boardSeq(board.boardSeq())
                            .userId(board.userId())
                            .readCnt(board.readCnt())
                            .title(board.title())
                            .contents(board.contents())
                            .regId(board.regId())
                            .regDt(board.regDt())
                            .chgId(board.chgId())
                            .chgDt(board.chgDt())
                            .build();
                    updatedList.add(updatedBoard);
                } else {
                    updatedList.add(board);
                }
            } catch (Exception e) {
                log.error("Error getting user info for userId: " + board.userId(), e);
                updatedList.add(board);
            }
        }

        log.info(this.getClass().getName() + ".getBoardList End!");
        return updatedList;
    }


    public Page<BoardDTO> getBoardList(Pageable pageable) {
        Page<BoardEntity> entityPage = boardRepository.findAllByOrderByBoardSeqDesc(pageable);
        List<BoardDTO> dtoList = entityPage.getContent().stream()
                .map(entity -> new ObjectMapper().convertValue(entity, BoardDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }









    @Transactional
    @Override
    public BoardDTO getBoard(BoardDTO pDTO, boolean type) {
        log.info(this.getClass().getName() + ".getBoard Start!");

        if (type) {
            int res = boardRepository.updateReadCnt(pDTO.boardSeq());
            log.info("res : " + res);
        }

        BoardEntity rEntity = boardRepository.findByBoardSeq(pDTO.boardSeq());
        BoardDTO rDTO = new ObjectMapper().convertValue(rEntity, BoardDTO.class);

        log.info(this.getClass().getName() + ".getBoard End!");
        return rDTO;
    }

    @Override
    public void insertBoard(BoardDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".InsertBoardInfo Start!");

        String userId = CmmUtil.nvl(pDTO.userId());
        String title = CmmUtil.nvl(pDTO.title());
        String contents = CmmUtil.nvl(pDTO.contents());


        BoardEntity pEntity = BoardEntity.builder()
                .userId(userId)
                .title(title)
                .contents(contents)
                .readCnt(0L)
                .regId(userId)
                .regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgId(userId)
                .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        boardRepository.save(pEntity);

        log.info(this.getClass().getName() + ".InsertBoardInfo End!");
    }

    @Transactional
    @Override
    public void updateBoard(BoardDTO pDTO) {
        log.info(this.getClass().getName() + ".updateBoard Start!");

        Long boardSeq = pDTO.boardSeq();
        String userId = CmmUtil.nvl(pDTO.userId());
        String title = CmmUtil.nvl(pDTO.title());
        String contents = CmmUtil.nvl(pDTO.contents());

        BoardEntity rEntity = boardRepository.findByBoardSeq(boardSeq);

        BoardEntity pEntity = BoardEntity.builder()
                .boardSeq(boardSeq)
                .userId(userId)
                .title(title)
                .contents(contents)
                .readCnt(rEntity.getReadCnt())
                .build();

        boardRepository.save(pEntity);

        log.info(this.getClass().getName() + ".updateBoard End!");
    }

    @Override
    public void deleteBoard(BoardDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".deleteBoard Start!");

        Long boardSeq = pDTO.boardSeq();
        log.info("myBoardSeq : " + boardSeq);

        boardRepository.deleteById(boardSeq);

        log.info(this.getClass().getName() + ".deleteBoard End!");
    }


}
