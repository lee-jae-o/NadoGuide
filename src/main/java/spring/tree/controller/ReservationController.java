package spring.tree.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.tree.dto.MsgDTO;
import spring.tree.dto.ReservationDTO;
import spring.tree.dto.TourDTO;
import spring.tree.service.IReservationService;
import spring.tree.service.ITourService;
import spring.tree.util.CmmUtil;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;
    private final ITourService tourService;

    @ResponseBody
    @PostMapping("/make")
    public MsgDTO makeReservation(HttpServletRequest request, HttpSession session) {
        log.info("makeReservation Start!");

        String msg = "";
        MsgDTO dto;

        try {
            Long tourSeq = Long.parseLong(CmmUtil.nvl(request.getParameter("tourSeq")));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            TourDTO tour = tourService.getTour(TourDTO.builder().tourSeq(tourSeq).build(), false);
            int reservedCount = reservationService.getReservedCount(tourSeq);

            if (reservedCount >= tour.maxParticipants()) {
                msg = "남은 자리가 없습니다.";
            } else {
                ReservationDTO reservationDTO = ReservationDTO.builder()
                        .tourSeq(tourSeq)
                        .userId(userId)
                        .reservationDate(LocalDateTime.now())
                        .build();

                reservationService.makeReservation(reservationDTO);

                msg = "예약이 완료되었습니다.";
            }
        } catch (Exception e) {
            msg = "예약에 실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info("makeReservation End!");
        }

        return dto;
    }



    @ResponseBody
    @PostMapping("/cancel")
    public MsgDTO cancelReservation(HttpServletRequest request, HttpSession session) {
        log.info("cancelReservation Start!");

        String msg = "";
        MsgDTO dto;

        try {
            Long reservationId = Long.parseLong(CmmUtil.nvl(request.getParameter("reservationId")));

            reservationService.cancelReservation(reservationId);

            msg = "예약이 취소되었습니다.";
        } catch (Exception e) {
            msg = "예약 취소에 실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info("cancelReservation End!");
        }

        return dto;
    }
}


