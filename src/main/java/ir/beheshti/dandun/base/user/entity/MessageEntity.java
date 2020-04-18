package ir.beheshti.dandun.base.user.entity;

import ir.beheshti.dandun.base.socket.ChatMessageType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MessageTable")
public class MessageEntity {
    @Id
    @Column(name = "MessageId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int messageId;
    @Column(name = "ChatId")
    private int chatId;
    @Column(name = "MessageText", columnDefinition = "TEXT")
    private String message;
    @Column(name = "UserId")
    private int userId;
    @Enumerated(value = EnumType.STRING)
    private ChatMessageType chatMessageType;
    private long timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ChatId", referencedColumnName = "ChatId", insertable = false, updatable = false)
    private ChatEntity chatEntity;
}
