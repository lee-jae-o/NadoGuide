package spring.tree.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.tree.dto.FavoriteTourDTO;
import spring.tree.dto.MsgDTO;
import spring.tree.service.impl.FavoriteTourService;
import spring.tree.util.CmmUtil;

@Slf4j
@Controller
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteTourController {

    private final FavoriteTourService favoriteTourService;

    @ResponseBody
    @PostMapping("/add")
    public MsgDTO addFavorite(HttpServletRequest request, HttpSession session) {
        log.info("addFavorite Start!");

        String msg = "";
        MsgDTO dto;

        try {
            Long tourSeq = Long.parseLong(CmmUtil.nvl(request.getParameter("tourSeq")));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            if (favoriteTourService.isFavorite(userId, tourSeq)) {
                msg = "이미 찜한 투어입니다.";
            } else {
                FavoriteTourDTO favoriteTourDTO = FavoriteTourDTO.builder()
                        .tourSeq(tourSeq)
                        .userId(userId)
                        .build();

                favoriteTourService.addFavoriteTour(favoriteTourDTO);

                msg = "투어가 찜 목록에 추가되었습니다.";
            }
        } catch (Exception e) {
            msg = "찜하기에 실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info("addFavorite End!");
        }

        return dto;
    }

    @ResponseBody
    @PostMapping("/remove")
    public MsgDTO removeFavorite(HttpServletRequest request, HttpSession session) {
        log.info("removeFavorite Start!");

        String msg = "";
        MsgDTO dto;

        try {
            Long tourSeq = Long.parseLong(CmmUtil.nvl(request.getParameter("tourSeq")));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            favoriteTourService.removeFavoriteTour(userId, tourSeq);

            msg = "찜 목록에서 제거되었습니다.";
        } catch (Exception e) {
            msg = "찜 목록에서 제거하는데 실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info("removeFavorite End!");
        }

        return dto;
    }
}
