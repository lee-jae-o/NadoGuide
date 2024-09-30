package spring.tree.repository;

import spring.tree.repository.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {

    List<CalendarEntity> findAllByUserIdOrderByCalendarSeqDesc(String userId);

    CalendarEntity findByCalendarSeq(Long calendarSeq);
}
