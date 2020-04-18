//package ir.beheshti.dandun.base.user.entity;
//
//import lombok.Data;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@Table(name = "UserMessage")
//public class UserMessageEntity {
//    @Id
//    @Column(name = "UserMessageId")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int userMessageId;
//
//    @Column(name = "FromUserId")
//    private int fromUserId;
//    @Column(name = "ToUserId")
//    private int toUserId;
//    @Column(name = "MessageId")
//    private int messageId;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "MessageId", referencedColumnName = "MessageId",
//            insertable = false, updatable = false)
//    private MessageEntity messageEntity;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "ToUserId", referencedColumnName = "UserId",
//            insertable = false, updatable = false)
//    private UserEntity toUserEntity;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "FromUserId", referencedColumnName = "UserId",
//            insertable = false, updatable = false)
//    private UserEntity fromUserEntity;
//}
//
