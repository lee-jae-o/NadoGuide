package spring.tree.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.tree.dto.EventDTO;
import spring.tree.service.IFeignService;
import spring.tree.service.impl.FeignService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/feign")
public class FeignController {

    private final IFeignService feignService;

    @GetMapping(value = "/seoulEvent")
    public String getSeoulEvent(Model model) {
        List<EventDTO> events = feignService.getSeoulEvents();
        System.out.println("Events: " + events); // 로깅
        model.addAttribute("events", events);
        return "feign/seoulEvent";
    }


    @GetMapping(value = "/seoulSpace")
    public String getSeoulSpace(Model model) {
        // 문화 공간 정보를 모델에 추가하여 뷰에 전달
        model.addAttribute("spaces", feignService.getSeoulSpaces());
        return "feign/seoulSpace"; // Thymeleaf 템플릿 경로
    }

}
