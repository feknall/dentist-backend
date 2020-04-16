package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.InformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationRepository extends JpaRepository<InformationEntity, Integer> {

}
