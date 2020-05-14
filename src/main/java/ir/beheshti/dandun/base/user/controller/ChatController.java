package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.socket.ChatOutputDto;
import ir.beheshti.dandun.base.socket.ChatService;
import ir.beheshti.dandun.base.user.dto.socket.MessageOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Chat")
@RestController
@RequestMapping(path = "/api/v1/user/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<Integer>> getUserChatIds() {
        return ResponseEntity.ok(chatService.getUserChatIds());
    }

    @GetMapping(path = "/{chatId}")
    public ResponseEntity<List<MessageOutputDto>> getUserMessageList(@PathVariable int chatId) {
        return ResponseEntity.ok(chatService.getChatMessagesHistory(chatId));
    }

    @GetMapping(path = "/history")
    public ResponseEntity<List<ChatOutputDto>> getChatHistory() {
        return ResponseEntity.ok(chatService.getChatHistory());
    }
}
