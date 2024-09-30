package spring.tree.controller;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import spring.tree.repository.entity.Chat;
import spring.tree.repository.entity.Room;
import spring.tree.service.impl.ChatService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final ChatService chatService;

    /**
     * 채팅방 참여하기
     * @param roomId 채팅방 id
     */
//    @GetMapping("/{roomId}")
//    public String joinRoom(@PathVariable(required = false) Long roomId, Model model) {
//        List<Chat> chatList = chatService.findAllChatByRoomId(roomId);
//
//        model.addAttribute("roomId", roomId);
//        model.addAttribute("chatList", chatList);
//        return "chat/room";
//    }

    @GetMapping("/{roomId}")
    public String joinRoom(@PathVariable(required = false) Long roomId, Model model) {
        Room room = chatService.findRoomById(roomId); // 채팅방 정보 가져오기
        List<Chat> chatList = chatService.findAllChatByRoomId(roomId);

        model.addAttribute("roomId", roomId);
        model.addAttribute("roomName", room.getName()); // 채팅방 이름을 모델에 추가
        model.addAttribute("chatList", chatList);

        return "chat/room";
    }



    /**
     * 채팅방 등록
     * @param form
     */
    @PostMapping("/room")
    public String createRoom(RoomForm form) {
        chatService.createRoom(form.getName());
        return "redirect:/roomList";
    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/roomList")
    public String roomList(Model model) {
        List<Room> roomList = chatService.findAllRoom();
        model.addAttribute("roomList", roomList);
        return "chat/roomList";
    }

    /**
     * 방만들기 폼
     */
    @GetMapping("/roomForm")
    public String roomForm() {
        return "chat/roomForm";
    }

}
