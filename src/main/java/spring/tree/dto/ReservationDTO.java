package spring.tree.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record ReservationDTO(
        Long reservationId,
        Long tourSeq,
        String userId,
        LocalDateTime reservationDate

) {
}
