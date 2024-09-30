package spring.tree.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.tree.repository.entity.Chat;
import spring.tree.dto.ChatMessage;
import spring.tree.service.impl.ChatService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}") //여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/room/{roomId}")   //구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public ChatMessage test(@DestinationVariable Long roomId, ChatMessage message) {

        //채팅 저장
        Chat chat = chatService.createChat(roomId, message.getSender(), message.getMessage());
        return ChatMessage.builder()
                .roomId(roomId)
                .sender(chat.getSender())
                .message(chat.getMessage())
                .build();
    }

    @PostMapping("/send/{roomId}")
    @ResponseBody
    public void sendMessage(@PathVariable Long roomId, @RequestBody ChatMessage message, HttpSession session) {
        String userNickname = (String) session.getAttribute("SS_USER_NICKNAME");  // 세션에서 닉네임 가져오기
        if (userNickname != null) {
            message.setSender(userNickname);  // 메시지의 sender에 닉네임 설정
            chatService.createChat(roomId, message.getSender(), message.getMessage());
        } else {
            throw new IllegalStateException("User is not logged in or nickname not found");
        }
    }

}