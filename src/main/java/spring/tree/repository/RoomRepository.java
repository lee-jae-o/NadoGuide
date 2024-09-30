package spring.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.tree.repository.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}