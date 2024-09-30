package spring.tree.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CulturalSpaceResponseDTO {

    @JsonProperty("culturalSpaceInfo")
    private CulturalSpaceInfo culturalSpaceInfo;

    @Data
    public static class CulturalSpaceInfo {
        @JsonProperty("list_total_count")
        private int listTotalCount;

        @JsonProperty("RESULT")
        private Result result;

        @JsonProperty("row")
        private List<SpaceDTO> row;
    }

    @Data
    public static class Result {
        @JsonProperty("CODE")
        private String code;

        @JsonProperty("MESSAGE")
        private String message;
    }
}
