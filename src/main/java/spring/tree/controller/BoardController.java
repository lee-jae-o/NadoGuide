package spring.tree.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import spring.tree.dto.*;
import spring.tree.service.ICommentService;
import spring.tree.service.impl.BoardService;
import spring.tree.service.impl.UserInfoService;
import spring.tree.util.CmmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/board")
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final UserInfoService userInfoService;
    private final ICommentService commentService;

    @GetMapping(value = "boardList")
    public String boardList(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "5") int size,
                            HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".boardList Start!");

        String SS_USER_ID = (String) session.getAttribute("SS_USER_ID");
        log.info("SS_USER_ID : " + SS_USER_ID);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardDTO> boardPage = boardService.getBoardList(pageable);

        model.addAttribute("SS_USER_ID", SS_USER_ID);
        model.addAttribute("rList", boardPage.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", boardPage.getTotalPages());

        log.info(this.getClass().getName() + ".boardList End!");

        return "board/boardList";
    }


    @GetMapping(value = "boardReg")
    public String boardReg() {

        log.info(this.getClass().getName() + ".boardReg Start!");

        log.info(this.getClass().getName() + ".boardReg End!");

        return "board/boardReg";
    }

    @ResponseBody
    @PostMapping(value = "boardInsert")
    public MsgDTO boardInsert(HttpServletRequest request, HttpSession session) {
        log.info(this.getClass().getName() + ".boardInsert Start!");

        String msg = "";
        MsgDTO dto;

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));

            BoardDTO pDTO = BoardDTO.builder()
                    .title(title).contents(contents)
                    .userId(userId).build();

            boardService.insertBoard(pDTO);

            msg = "등록되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info(this.getClass().getName() + ".boardInsert End!");
        }

        return dto;
    }

    @GetMapping(value = "boardInfo")
    public String boardInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".boardInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"), "0"); // 공지글번호(PK)

        log.info("nSeq : " + nSeq);

        BoardDTO pDTO = BoardDTO.builder()
                .boardSeq(Long.parseLong(nSeq))
                .build();

        CommentDTO cDTO = CommentDTO.builder()
                .boardSeq(Long.parseLong(nSeq))
                .build();

        BoardDTO rDTO = Optional.ofNullable(boardService.getBoard(pDTO, true))
                .orElseGet(() -> BoardDTO.builder().build());

        List<CommentDTO> rList = Optional.ofNullable(commentService.getCommentList(cDTO))
                .orElseGet(() -> new ArrayList<>());

        log.info("rDTO.noticeSeq : " + rDTO.boardSeq());

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);
        model.addAttribute("rList", rList);


        log.info(this.getClass().getName() + ".noticeInfo End!");

        return "board/boardInfo";
    }


    @GetMapping(value = "boardEditInfo")
    public String boardEditInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".boardEditInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글번호(PK)

        log.info("nSeq : " + nSeq);

        BoardDTO pDTO = BoardDTO.builder().boardSeq(Long.parseLong(nSeq)).build();

        BoardDTO rDTO = Optional.ofNullable(boardService.getBoard(pDTO, false))
                .orElseGet(() -> BoardDTO.builder().build());

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".boardEditInfo End!");

        return "board/boardEditInfo";
    }



    @ResponseBody
    @PostMapping(value = "boardUpdate")
    public MsgDTO boardUpdate(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".boardUpdate Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));

            BoardDTO pDTO = BoardDTO.builder().userId(userId).boardSeq(Long.parseLong(nSeq))
                    .title(title).contents(contents).build();

            boardService.updateBoard(pDTO);

            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".boardUpdate End!");

        }

        return dto;
    }


    @ResponseBody
    @PostMapping(value = "boardDelete")
    public MsgDTO boardDelete(HttpServletRequest request) {

        log.info(this.getClass().getName() + ".boardDelete Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));

            log.info("nSeq : " + nSeq);

            BoardDTO pDTO = BoardDTO.builder().boardSeq(Long.parseLong(nSeq)).build();

            boardService.deleteBoard(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".boardDelete End!");

        }

        return dto;
    }

}
