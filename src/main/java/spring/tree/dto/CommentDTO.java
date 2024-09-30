package spring.tree.dto;

import lombok.Builder;
@Builder
public record CommentDTO (

        Long boardSeq,
        Long commentSeq,
        String comment,
        String userId,
        String regDt,
        String chgDt

){

}
