package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientUserEntity, Integer> {

    List<PatientUserEntity> findAllByPatientStateType(PatientStateType patientStateType);
}
