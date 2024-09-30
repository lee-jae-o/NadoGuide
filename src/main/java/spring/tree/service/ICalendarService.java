package spring.tree.service;

import spring.tree.dto.CalendarDTO;

import java.util.List;

public interface ICalendarService {


    List<CalendarDTO> getCalendarList(String userId);


    void updateCalendarInfo(CalendarDTO pDTO) throws Exception;


    void deleteCalendarInfo(CalendarDTO pDTO) throws Exception;


    void insertCalendarInfo(CalendarDTO pDTO) throws Exception;


    void deleteCalendarInfoByUserId(String userId) throws Exception;


}
