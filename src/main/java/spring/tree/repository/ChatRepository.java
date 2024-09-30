package spring.tree.repository;

import org.springframework.data.repository.CrudRepository;
import spring.tree.repository.entity.Chat;

import java.util.List;

public interface ChatRepository extends CrudRepository<Chat, Long> {

    List<Chat> findAllByRoomId(Long roomId);
}
