package spring.tree.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.tree.dto.MsgDTO;
import spring.tree.dto.TourDTO;
import spring.tree.dto.UserInfoDTO;
import spring.tree.service.ITourService;
import spring.tree.service.impl.ImageUploadService;
import spring.tree.service.impl.ReservationService;
import spring.tree.service.impl.UserInfoService;
import spring.tree.util.CmmUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/tour")
@RequiredArgsConstructor
@Controller
public class TourController {

    private final ITourService tourService;
    private final ImageUploadService imageUploadService;
    private final UserInfoService userInfoService;
    private final ReservationService reservationService;

    @GetMapping(value = "tourList")
    public String tourList(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".tourList Start!");

        String SS_USER_ID = (String) session.getAttribute("SS_USER_ID");
        log.info("SS_USER_ID : " + SS_USER_ID);

        List<TourDTO> rList = Optional.ofNullable(tourService.getTourList()).orElseGet(ArrayList::new);

        model.addAttribute("SS_USER_ID", SS_USER_ID);
        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".tourList End!");

        return "tour/tourList";
    }

    @GetMapping(value = "tourReg")
    public String tourReg() {
        log.info(this.getClass().getName() + ".tourReg Start!");
        log.info(this.getClass().getName() + ".tourReg End!");
        return "tour/tourReg";
    }

    @ResponseBody
    @PostMapping(value = "tourInsert")
    public MsgDTO tourInsert(@RequestParam("image") MultipartFile image, HttpServletRequest request, HttpSession session) {
        log.info(this.getClass().getName() + ".tourInsert Start!");

        String msg = "";
        MsgDTO dto;

        try {
            String tourTitle = CmmUtil.nvl(request.getParameter("tourTitle"));
            String tourLocation = CmmUtil.nvl(request.getParameter("tourLocation"));
            String tourDate = CmmUtil.nvl(request.getParameter("tourDate"));
            int maxParticipants = Integer.parseInt(CmmUtil.nvl(request.getParameter("maxParticipants")));
            String tourTime = CmmUtil.nvl(request.getParameter("tourTime"));
            String supplies = CmmUtil.nvl(request.getParameter("supplies"));
            String categories = CmmUtil.nvl(request.getParameter("categories"));
            String tourContents = CmmUtil.nvl(request.getParameter("tourContents"));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            String imageUrl = null;
            if (!image.isEmpty()) {
                imageUrl = imageUploadService.upload(image);
            }

            TourDTO pDTO = TourDTO.builder()
                    .tourTitle(tourTitle)
                    .tourLocation(tourLocation)
                    .tourDate(LocalDateTime.parse(tourDate))
                    .maxParticipants(maxParticipants)
                    .tourTime(tourTime)
                    .supplies(supplies)
                    .categories(categories)
                    .tourContents(tourContents)
                    .userId(userId)
                    .imageUrl(imageUrl)
                    .build();

            tourService.insertTour(pDTO);

            msg = "등록되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info(this.getClass().getName() + ".tourInsert End!");
        }

        return dto;
    }


//    @GetMapping(value = "tourInfo")
//    public String tourInfo(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {
//
//        log.info(this.getClass().getName() + ".tourInfo Start!");
//
//        String tourSeq = CmmUtil.nvl(request.getParameter("tourSeq"), "0");
//        log.info("tourSeq : " + tourSeq);
//
//        TourDTO pDTO = TourDTO.builder().tourSeq(Long.parseLong(tourSeq)).build();
//        TourDTO rDTO = Optional.ofNullable(tourService.getTour(pDTO, true))
//                .orElseGet(() -> TourDTO.builder().build());
//
//        if (rDTO.userId() != null) {
//            try {
//                UserInfoDTO userInfo = userInfoService.getUserIdExists(UserInfoDTO.builder().userId(rDTO.userId()).build());
//                if ("Y".equals(userInfo.existsYn())) {
//                    rDTO = TourDTO.builder()
//                            .tourSeq(rDTO.tourSeq())
//                            .userId(rDTO.userId())
//                            .nickName(userInfo.nickName())
//                            .email(userInfo.email())
//                            .regId(rDTO.regId())
//                            .regDt(rDTO.regDt())
//                            .readCnt(rDTO.readCnt())
//                            .tourTitle(rDTO.tourTitle())
//                            .tourLocation(rDTO.tourLocation())
//                            .tourDate(rDTO.tourDate())
//                            .maxParticipants(rDTO.maxParticipants())
//                            .tourTime(rDTO.tourTime())
//                            .supplies(rDTO.supplies())
//                            .categories(rDTO.categories())
//                            .tourContents(rDTO.tourContents())
//                            .imageUrl(rDTO.imageUrl())
//                            .build();
//                }
//            } catch (Exception e) {
//                log.error("Error getting user info for userId: " + rDTO.userId(), e);
//            }
//        }
//
//        String loggedInUserId = (String) session.getAttribute("SS_USER_ID");
//        boolean isReserved = reservationService.isAlreadyReserved(rDTO.tourSeq(), loggedInUserId);
//
//        model.addAttribute("rDTO", rDTO);
//        model.addAttribute("loggedInUserId", loggedInUserId);
//        model.addAttribute("isReserved", isReserved);
//
//        log.info(this.getClass().getName() + ".tourInfo End!");
//        return "tour/tourInfo";
//    }

    @GetMapping(value = "tourInfo")
    public String tourInfo(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".tourInfo Start!");

        String tourSeq = CmmUtil.nvl(request.getParameter("tourSeq"), "0");
        log.info("tourSeq : " + tourSeq);

        TourDTO pDTO = TourDTO.builder().tourSeq(Long.parseLong(tourSeq)).build();
        TourDTO rDTO = Optional.ofNullable(tourService.getTour(pDTO, true))
                .orElseGet(() -> TourDTO.builder().build());

        if (rDTO.userId() != null) {
            try {
                UserInfoDTO userInfo = userInfoService.getUserIdExists(UserInfoDTO.builder().userId(rDTO.userId()).build());
                if ("Y".equals(userInfo.existsYn())) {
                    rDTO = TourDTO.builder()
                            .tourSeq(rDTO.tourSeq())
                            .userId(rDTO.userId())
                            .nickName(userInfo.nickName())
                            .email(userInfo.email())
                            .regId(rDTO.regId())
                            .regDt(rDTO.regDt())
                            .readCnt(rDTO.readCnt())
                            .tourTitle(rDTO.tourTitle())
                            .tourLocation(rDTO.tourLocation())
                            .tourDate(rDTO.tourDate())
                            .maxParticipants(rDTO.maxParticipants())
                            .tourTime(rDTO.tourTime())
                            .supplies(rDTO.supplies())
                            .categories(rDTO.categories())
                            .tourContents(rDTO.tourContents())
                            .imageUrl(rDTO.imageUrl())
                            .build();
                }
            } catch (Exception e) {
                log.error("Error getting user info for userId: " + rDTO.userId(), e);
            }
        }

        String loggedInUserId = (String) session.getAttribute("SS_USER_ID");
        boolean isReserved = reservationService.isAlreadyReserved(rDTO.tourSeq(), loggedInUserId);
        int reservedCount = reservationService.getReservedCount(rDTO.tourSeq());
        int remainingSeats = rDTO.maxParticipants() - reservedCount;

        model.addAttribute("rDTO", rDTO);
        model.addAttribute("loggedInUserId", loggedInUserId);
        model.addAttribute("isReserved", isReserved);
        model.addAttribute("remainingSeats", remainingSeats);

        log.info(this.getClass().getName() + ".tourInfo End!");
        return "tour/tourInfo";
    }




    @GetMapping(value = "tourEditInfo")
    public String tourEditInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".tourEditInfo Start!");

        String tourSeq = CmmUtil.nvl(request.getParameter("tourSeq"));

        log.info("tourSeq : " + tourSeq);

        TourDTO pDTO = TourDTO.builder().tourSeq(Long.parseLong(tourSeq)).build();

        TourDTO rDTO = Optional.ofNullable(tourService.getTour(pDTO, false))
                .orElseGet(() -> TourDTO.builder().build());

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".tourEditInfo End!");

        return "tour/tourEditInfo";
    }

    @ResponseBody
    @PostMapping(value = "tourUpdate", consumes = { "multipart/form-data" })
    public MsgDTO tourUpdate(@RequestParam("image") MultipartFile image, HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".tourUpdate Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String tourSeq = CmmUtil.nvl(request.getParameter("tourSeq"));
            String tourTitle = CmmUtil.nvl(request.getParameter("tourTitle"));
            String tourLocation = CmmUtil.nvl(request.getParameter("tourLocation"));
            String tourDate = CmmUtil.nvl(request.getParameter("tourDate"));
            int maxParticipants = Integer.parseInt(CmmUtil.nvl(request.getParameter("maxParticipants")));
            String tourTime = CmmUtil.nvl(request.getParameter("tourTime"));
            String supplies = CmmUtil.nvl(request.getParameter("supplies"));
            String categories = CmmUtil.nvl(request.getParameter("categories"));
            String tourContents = CmmUtil.nvl(request.getParameter("tourContents"));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            String imageUrl = null;
            if (!image.isEmpty()) {
                imageUrl = imageUploadService.upload(image);
            }

            TourDTO pDTO = TourDTO.builder()
                    .tourSeq(Long.parseLong(tourSeq))
                    .userId(userId)
                    .tourTitle(tourTitle)
                    .tourLocation(tourLocation)
                    .tourDate(LocalDateTime.parse(tourDate))
                    .maxParticipants(maxParticipants)
                    .tourTime(tourTime)
                    .supplies(supplies)
                    .categories(categories)
                    .tourContents(tourContents)
                    .imageUrl(imageUrl)
                    .build();

            tourService.updateTour(pDTO);

            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info(this.getClass().getName() + ".tourUpdate End!");
        }

        return dto;
    }

    @ResponseBody
    @PostMapping(value = "tourDelete")
    public MsgDTO tourDelete(HttpServletRequest request) {

        log.info(this.getClass().getName() + ".tourDelete Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String tourSeq = CmmUtil.nvl(request.getParameter("tourSeq"));

            log.info("tourSeq : " + tourSeq);

            TourDTO pDTO = TourDTO.builder().tourSeq(Long.parseLong(tourSeq)).build();

            tourService.deleteTour(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info(this.getClass().getName() + ".tourDelete End!");
        }

        return dto;
    }







    @ResponseBody
    @PostMapping("/delete")
    public MsgDTO deleteTour(HttpServletRequest request, HttpSession session) {
        log.info("deleteTour Start!");

        String msg = "";
        MsgDTO dto;

        try {
            Long tourSeq = Long.parseLong(CmmUtil.nvl(request.getParameter("tourSeq")));

            // 예약한 사용자가 있는지 확인
            int reservedCount = reservationService.getReservedCount(tourSeq);
            if (reservedCount > 0) {
                msg = "예약한 사용자가 있어 삭제가 불가능합니다.";
            } else {
                tourService.deleteTour(TourDTO.builder().tourSeq(tourSeq).build());
                msg = "투어가 삭제되었습니다.";
            }
        } catch (Exception e) {
            msg = "투어 삭제에 실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info("deleteTour End!");
        }

        return dto;
    }
}
