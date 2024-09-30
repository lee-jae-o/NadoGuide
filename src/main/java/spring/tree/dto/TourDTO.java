package spring.tree.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record TourDTO(
        Long tourSeq,
        String userId, // 사용자 아이디
        String nickName, // 사용자 닉네임
        String email, // 사용자 이메일
        String regId, // 등록자 아이디
        String regDt, // 등록일
        Long readCnt, // 조회수
        String tourTitle, // 투어 제목
        String tourLocation, // 투어 장소
        LocalDateTime tourDate, // 투어 날짜,시간
        int maxParticipants, // 최대 참여 가능 인원
        String tourTime, // 투어 소요 시간
        String supplies, // 준비물
        String categories, // 카테고리
        String tourContents, // 투어 내용
        String imageUrl // 이미지 url

) {
}







