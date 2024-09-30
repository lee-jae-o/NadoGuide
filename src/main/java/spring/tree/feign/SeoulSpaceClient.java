package spring.tree.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring.tree.dto.CulturalSpaceResponseDTO;

@FeignClient(
        name = "seoulSpaceClient",
        url = "${feign.client.seoul-space.url}",
        configuration = spring.tree.config.OpenFeignConfig.class
)
public interface SeoulSpaceClient {

    // 서울시 문화 공간 정보 가져오기
    @GetMapping("/{start}/{end}")
    CulturalSpaceResponseDTO getSeoulSpaces(
            @PathVariable("start") int start,
            @PathVariable("end") int end
    );
}
