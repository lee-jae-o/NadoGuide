package spring.tree.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.tree.dto.CulturalSpaceResponseDTO;
import spring.tree.dto.EventDTO;
import spring.tree.dto.EventResponseDTO;
import spring.tree.dto.SpaceDTO;
import spring.tree.feign.SeoulEventClient;
import spring.tree.feign.SeoulSpaceClient;
import spring.tree.service.IFeignService;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FeignService implements IFeignService {

    private final SeoulEventClient seoulEventClient;
    private final SeoulSpaceClient seoulSpaceClient;
    private final Random random = new Random();

    @Override
    public List<EventDTO> getSeoulEvents() {
        // 총 이벤트 개수 가져오기
        EventResponseDTO initialResponse = seoulEventClient.getSeoulEvents(1, 1);
        int totalCount = initialResponse.getCulturalEventInfo().getListTotalCount();

        // 랜덤한 시작 지점 설정
        int start = random.nextInt(totalCount - 10) + 1; // 1부터 (총 개수 - 10) 사이의 랜덤 수
        int end = start + 9; // 10개의 아이템을 가져오기 위한 끝 범위 설정

        // OpenFeign을 통해 랜덤한 이벤트 정보 가져오기
        EventResponseDTO response = seoulEventClient.getSeoulEvents(start, end);

        // 가져온 데이터를 DTO 리스트로 변환하여 반환
        return response.getCulturalEventInfo().getRow();
    }


    @Override
    public List<SpaceDTO> getSeoulSpaces() {
        // 총 데이터 개수 가져오기
        CulturalSpaceResponseDTO initialResponse = seoulSpaceClient.getSeoulSpaces(1, 1);
        int totalCount = initialResponse.getCulturalSpaceInfo().getListTotalCount();

        // 랜덤한 시작 지점 설정
        int start = random.nextInt(totalCount - 10) + 1; // 1부터 (총 개수 - 10) 사이의 랜덤 수
        int end = start + 9; // 10개의 아이템을 가져오기 위한 끝 범위 설정

        // OpenFeign을 통해 랜덤한 문화 공간 정보 가져오기
        CulturalSpaceResponseDTO response = seoulSpaceClient.getSeoulSpaces(start, end);

        // 가져온 데이터를 DTO 리스트로 변환하여 반환
        return response.getCulturalSpaceInfo().getRow();
    }


}
