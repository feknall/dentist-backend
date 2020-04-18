package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.ChatEntity;
import ir.beheshti.dandun.base.user.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Integer> {
    List<ChatEntity> findAllByPatientId(Integer patientId);
    List<ChatEntity> findAllByDoctorId(Integer doctorId);
}
