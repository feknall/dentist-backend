package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientUserEntity, Integer> {
}