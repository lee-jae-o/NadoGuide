package spring.tree.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EventResponseDTO {
    @JsonProperty("culturalEventInfo")
    private CulturalEventInfo culturalEventInfo;

    @Data
    public static class CulturalEventInfo {
        @JsonProperty("list_total_count")
        private int listTotalCount;

        @JsonProperty("RESULT")
        private Result result;

        @JsonProperty("row")
        private List<EventDTO> row;
    }

    @Data
    public static class Result {
        @JsonProperty("CODE")
        private String code;

        @JsonProperty("MESSAGE")
        private String message;
    }
}
