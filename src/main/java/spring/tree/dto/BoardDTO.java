package spring.tree.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record BoardDTO(

        Long boardSeq,
        String userId,
        String title,
        String contents,
        Long readCnt,
        String regId, // 등록자 아이디
        String regDt, // 등록일
        String chgId, // 수정자 아이디
        String chgDt // 수정일

){

}
