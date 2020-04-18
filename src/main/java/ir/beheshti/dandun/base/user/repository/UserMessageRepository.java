package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.UserMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessageEntity, Integer> {
    List<UserMessageEntity> findAllByFromUserId(Integer fromUserId);
    List<UserMessageEntity> findAllByToUserId(Integer toUserId);
    List<UserMessageEntity> findAllByFromUserIdAndToUserId(Integer fromUserId, Integer toUserId);
}
