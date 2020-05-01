package ir.beheshti.dandun.base.information;

import ir.beheshti.dandun.base.information.InformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationRepository extends JpaRepository<InformationEntity, Integer> {

}
