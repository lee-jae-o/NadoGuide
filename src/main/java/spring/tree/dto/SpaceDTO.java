package spring.tree.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpaceDTO(
        String FAC_NAME,
        String ADDR,
        double X_COORD,
        double Y_COORD,
        String PHNE,
        String HOMEPAGE,
        String CLOSEDAY,
        String ENTRFREE,
        String MAIN_IMG,
        String FAC_DESC
) {
}
