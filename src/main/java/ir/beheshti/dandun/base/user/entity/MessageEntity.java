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
    @Column(name = "message", columnDefinition="TEXT")
    private String message;
    @Column(name = "fromUserId")
    private Integer fromUserId;
    @Column(name = "toUserId")
    private Integer toUserId;
    @Enumerated(value = EnumType.STRING)
    private ChatMessageType chatMessageType;
    private long timestamp;

    @ManyToOne
    @JoinColumn(name = "fromUserId", referencedColumnName = "userId",
            insertable = false, updatable = false)
    private UserEntity fromUserEntity;

    @ManyToOne
    @JoinColumn(name = "toUserId", referencedColumnName = "userId",
            insertable = false, updatable = false)
    private UserEntity toUserEntity;
}
