package spring.tree.service.impl;


import spring.tree.dto.CalendarDTO;
import spring.tree.repository.CalendarRepository;
import spring.tree.repository.entity.CalendarEntity;
import spring.tree.service.ICalendarService;
import spring.tree.util.CmmUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalendarService implements ICalendarService {

    private final CalendarRepository calendarRepository;

    @Override
    public List<CalendarDTO> getCalendarList(String userId) {
        log.info("Fetching calendar data for user: {}", userId);

        List<CalendarEntity> rList = calendarRepository.findAllByUserIdOrderByCalendarSeqDesc(userId);

        List<CalendarDTO> nList = rList.stream()
                .map(calendarEntity -> CalendarDTO.builder()
                        .calendarSeq(calendarEntity.getCalendarSeq())
                        .title(calendarEntity.getTitle())
                        .userId(calendarEntity.getUserId())
                        .start(calendarEntity.getStart())
                        .end(calendarEntity.getEnd())
                        .description(calendarEntity.getDescription())
                        .build())
                .collect(Collectors.toList());

        log.info("Calendar data fetched successfully for user: {}", userId);

        return nList;
    }


    @Override
    public void updateCalendarInfo(CalendarDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".updateCalendarInfo Start!");

        Long calendarSeq = pDTO.calendarSeq();

        String title = CmmUtil.nvl(pDTO.title());
        String userId = CmmUtil.nvl(pDTO.userId());
        String start = CmmUtil.nvl(pDTO.start());
        String end = CmmUtil.nvl(pDTO.end());
        String description = CmmUtil.nvl(pDTO.description());

        log.info("calendarSeq : " + calendarSeq);
        log.info("userId : " + userId);
        log.info("title : " + title);
        log.info("start : " + start);
        log.info("end : " + end);
        log.info("description : " + description);

        CalendarEntity pEntity = CalendarEntity.builder()
                .calendarSeq(calendarSeq).title(title).userId(userId).start(start).end(end).description(description)
                .build();


        calendarRepository.save(pEntity);

    }

    @Override
    public void deleteCalendarInfo(CalendarDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteCalendarInfo Start!");

        Long calendarSeq = pDTO.calendarSeq();

        log.info("CalendarSeq : " + calendarSeq);

        calendarRepository.deleteById(calendarSeq);

        log.info(this.getClass().getName() + ".deleteCalendarInfo End!");

    }

    @Override
    public void insertCalendarInfo(CalendarDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".InsertNoticeInfo Start!");

        String title = CmmUtil.nvl(pDTO.title());
        String userId = CmmUtil.nvl(pDTO.userId());
        String start = CmmUtil.nvl(pDTO.start());
        String end = CmmUtil.nvl(pDTO.end());
        String description = CmmUtil.nvl(pDTO.description());

        log.info("title : " + title);
        log.info("userId : " + userId);
        log.info("start : " + start);
        log.info("end : " + end);
        log.info("description : " + description);


        CalendarEntity pEntity = CalendarEntity.builder()
                .title(title).userId(userId).start(start).end(end).description(description)
                .build();

        calendarRepository.save(pEntity);

        log.info(this.getClass().getName() + ".InsertCalendarInfo End!");

    }

    @Override
    @Transactional
    public void deleteCalendarInfoByUserId(String userId) throws Exception {
        log.info(this.getClass().getName() + ".deleteCalendarInfoByUserId Start!");

        List<CalendarEntity> calendarList = calendarRepository.findAllByUserIdOrderByCalendarSeqDesc(userId);

        for (CalendarEntity entity : calendarList) {
            calendarRepository.delete(entity);
        }

        log.info(this.getClass().getName() + ".deleteCalendarInfoByUserId End!");
    }





}

