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
//
//    @Id
//    @Column(name = "UserMessageId")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int userMessageId;
//
//    @Column(name = "UserId")
//    private int userId;
//
//    @Column(name = "MessageId")
//    private int MessageId;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "MessageId", referencedColumnName = "MessageId",
//            insertable = false, updatable = false)
//    private MessageEntity messageEntity;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "UserId", referencedColumnName = "UserId",
//            insertable = false, updatable = false)
//    private UserEntity userEntity;
//}
//
