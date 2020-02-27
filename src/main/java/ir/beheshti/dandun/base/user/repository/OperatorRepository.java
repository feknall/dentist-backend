package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.OperatorUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperatorRepository extends JpaRepository<OperatorUserEntity, Integer> {

}
