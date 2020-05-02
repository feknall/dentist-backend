package ir.beheshti.dandun.base.socket;

import ir.beheshti.dandun.base.user.dto.socket.MessageOutputDto;
import ir.beheshti.dandun.base.user.entity.ChatEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChatOutputDto {
    private Integer chatId;
    private Integer patientId;
    private Integer doctorId;
    private List<MessageOutputDto> messageOutputDtoList;

    public static ChatOutputDto fromEntity(ChatEntity entity) {
        ChatOutputDto dto = new ChatOutputDto();
        dto.setChatId(entity.getChatId());
        dto.setDoctorId(entity.getDoctorId());
        dto.setPatientId(entity.getPatientId());
        dto.setMessageOutputDtoList(entity
                .getMessageEntityList()
                .stream()
                .map(MessageOutputDto::fromEntity)
                .collect(Collectors.toList()));
        return dto;
    }
}
