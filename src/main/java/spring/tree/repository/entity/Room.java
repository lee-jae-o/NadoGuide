package spring.tree.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;
    private String name;

    @Builder
    public Room(String name) {
        this.name = name;
    }

    /**
     * 채팅방 생성
     * @param name 방 이름
     * @return Room Entity
     */
    public static Room createRoom(String name) {
        return Room.builder()
                .name(name)
                .build();
    }

}