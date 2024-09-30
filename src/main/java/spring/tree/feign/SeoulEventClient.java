package spring.tree.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring.tree.dto.EventResponseDTO;

@FeignClient(
        name = "seoulEventClient",
        url = "${feign.client.seoul-event.url}",
        configuration = spring.tree.config.OpenFeignConfig.class
)
public interface SeoulEventClient {

    @GetMapping("/{start}/{end}")
    EventResponseDTO getSeoulEvents(
            @PathVariable("start") int start,
            @PathVariable("end") int end
    );
}
