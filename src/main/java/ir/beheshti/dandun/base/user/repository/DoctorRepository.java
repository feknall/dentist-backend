package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorUserEntity, Integer> {

    List<DoctorUserEntity> findAllByDoctorStateType(DoctorStateType doctorStateType);
}
