package spring.tree.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.tree.dto.TourDTO;
import spring.tree.dto.UserInfoDTO;
import spring.tree.service.impl.TourService;
import spring.tree.service.impl.UserInfoService;

import java.util.List;

@Slf4j
@RequestMapping(value = "/")
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final TourService tourService;
    private final UserInfoService userInfoService;

    @GetMapping(value = "/")
    public String index(HttpSession session, Model model) {

        log.info("메인페이지 시작.");

        String userId = (String) session.getAttribute("SS_USER_ID");
        if (userId != null) {
            UserInfoDTO pDTO = UserInfoDTO.builder().userId(userId).build();
            try {
                UserInfoDTO userInfo = userInfoService.getUserIdExists(pDTO);
                model.addAttribute("SS_USER_NICKNAME", userInfo.nickName());
            } catch (Exception e) {
                log.error("Error getting user info", e);
            }
        }

        List<TourDTO> rList = tourService.getTourList();
        model.addAttribute("rList", rList);

        return "home";
    }
}
