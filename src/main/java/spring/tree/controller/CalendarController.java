package spring.tree.controller;

import spring.tree.dto.CalendarDTO;
import spring.tree.dto.MsgDTO;
import spring.tree.dto.UserInfoDTO;
import spring.tree.service.ICalendarService;
import spring.tree.service.impl.UserInfoService;
import spring.tree.util.CmmUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequestMapping(value = "/calendar")
@RequiredArgsConstructor
@Controller
public class CalendarController {


    private final ICalendarService calendarService;
    private final UserInfoService userInfoService;


    @GetMapping(value = "info")
    public String calendarinfo(HttpSession session, ModelMap model) throws Exception {

        log.info("Fetching calendar data from the database...");

        String userId = (String) session.getAttribute("SS_USER_ID");
        log.info("User ID: " + userId);

        List<CalendarDTO> rList = Optional.ofNullable(calendarService.getCalendarList(userId))
                .orElseGet(ArrayList::new);

        model.addAttribute("rList", rList);

        log.info("확인" + rList);

        return "calendar/info";
    }


    @ResponseBody
    @PostMapping(value = "calendarInsert")
    public MsgDTO calendarInsert(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".calendarInsert Start!");

        String msg = "";

        MsgDTO dto;

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String start = CmmUtil.nvl(request.getParameter("start")); // 공지글 여부
            String end = CmmUtil.nvl(request.getParameter("end")); // 내용
            String description = CmmUtil.nvl(request.getParameter("description")); // 내용

            log.info("session user_id : " + userId);
            log.info("title : " + title);
            log.info("start : " + start);
            log.info("end : " + end);
            log.info("description : " + description);

            CalendarDTO pDTO = CalendarDTO.builder().userId(userId).title(title)
                    .start(start).end(end).description(description).build();

            calendarService.insertCalendarInfo(pDTO);

            msg = "등록되었습니다.";

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".CalendarInsert End!");
        }

        return dto;
    }


    @ResponseBody
    @PostMapping(value = "calendarUpdate")
    public MsgDTO calemdarUpdate(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".calendarUpdate Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // 아이디
            String calendarSeq = CmmUtil.nvl(request.getParameter("calendarSeq")); // 글번호(PK)
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String start = CmmUtil.nvl(request.getParameter("start")); // 공지글 여부
            String end = CmmUtil.nvl(request.getParameter("end")); // 내용
            String description = CmmUtil.nvl(request.getParameter("description"));

            log.info("userId : " + userId);
            log.info("calendarSeq : " + calendarSeq);
            log.info("title : " + title);
            log.info("start : " + start);
            log.info("end : " + end);
            log.info("description : " + description);

            CalendarDTO pDTO = CalendarDTO.builder().userId(userId).calendarSeq(Long.parseLong(calendarSeq))
                    .title(title).start(start).end(end).description(description).build();

            calendarService.updateCalendarInfo(pDTO);

            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".calendarUpdate End!");

        }

        return dto;
    }
    @ResponseBody
    @PostMapping(value = "calendarDelete")
    public MsgDTO calendarDelete(HttpServletRequest request) {

        log.info(this.getClass().getName() + ".calendarDelete Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String calendarSeq = CmmUtil.nvl(request.getParameter("calendarSeq")); // 글번호(PK)

            log.info("calendarSeq : " + calendarSeq);


            CalendarDTO pDTO = CalendarDTO.builder().calendarSeq(Long.parseLong(calendarSeq)).build();

            calendarService.deleteCalendarInfo(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".calendarDelete End!");

        }
        return dto;
    }


//    @PostMapping(value = "/withdrawal")
//    public String withdrawal(HttpServletRequest request, HttpSession session) {
//        log.info(this.getClass().getName() + ".withdrawal Start!");
//
//        String userId = (String) session.getAttribute("SS_USER_ID");
//
//        try {
//
//            userInfoService.deleteUserInfo(userId);
//
//            calendarService.deleteCalendarInfoByUserId(userId);
//
//            session.invalidate();
//
//            return "redirect:/withdrawalSuccess";
//        } catch (Exception e) {
//
//            log.error("Error in withdrawal process: " + e.getMessage());
//
//            return "redirect:/errorPage";
//        }
//    }

    @PostMapping(value = "/withdrawal")
    public String withdrawal(HttpServletRequest request, HttpSession session) {
        log.info(this.getClass().getName() + ".withdrawal Start!");

        String userId = (String) session.getAttribute("SS_USER_ID");

        try {
            // UserInfoDTO 객체 생성
            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .build();

            // UserInfoDTO 객체를 deleteUserInfo 메서드에 전달
            userInfoService.deleteUserInfo(pDTO);

            calendarService.deleteCalendarInfoByUserId(userId);

            session.invalidate();

            return "redirect:/withdrawalSuccess";
        } catch (Exception e) {
            log.error("Error in withdrawal process: " + e.getMessage());
            return "redirect:/errorPage";
        }
    }


}
