package ir.beheshti.dandun.base.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationGroupRepository extends JpaRepository<NotificationGroupEntity, Integer> {
    Optional<NotificationGroupEntity> findByName(String name);
}
