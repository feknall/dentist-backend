package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity, Integer> {
    List<UserNotificationEntity> findAllByUserId(int userId);
}
