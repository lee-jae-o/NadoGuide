package spring.tree.service;

import spring.tree.dto.EventDTO;
import spring.tree.dto.SpaceDTO;

import java.util.List;

public interface IFeignService {
    // 서울시 행사 정보를 가져오는 메서드
    List<EventDTO> getSeoulEvents();

    List<SpaceDTO> getSeoulSpaces();
}
