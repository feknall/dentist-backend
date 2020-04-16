package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
    List<MessageEntity> findAllByFromUserIdAndToUserIdOrderByTimestamp(Integer fromUserId, Integer toUserId);
}
