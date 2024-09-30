package spring.tree.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Slf4j
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Controller
public class ApiController {



    @GetMapping(value = "map")
    public String map() {
        log.info(this.getClass().getName() + ".api/map Start!");
        log.info(this.getClass().getName() + ".api/map End!");
        return "api/map";
    }

    @GetMapping(value = "jobType")
    public String flexWorkStatus() {
        log.info("jobType 시작.");
        return "api/jobType";
    }








}
