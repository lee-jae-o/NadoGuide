package spring.tree.dto;

import lombok.Builder;

@Builder
public record EventDTO(
        String CODENAME,
        String GUNAME,
        String TITLE,
        String DATE,
        String PLACE,
        String ORG_NAME,
        String USE_TRGT,
        String USE_FEE,
        String PLAYER,
        String PROGRAM,
        String ETC_DESC,
        String ORG_LINK,
        String MAIN_IMG,
        String RGSTDATE,
        String TICKET,
        String STRTDATE,
        String END_DATE,
        String THEMECODE,
        String LOT,
        String LAT,
        String IS_FREE,
        String HMPG_ADDR
) {
}
