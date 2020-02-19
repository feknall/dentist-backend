package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.EssentialQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EssentialQuestionRepository extends JpaRepository<EssentialQuestionEntity, Integer> {

}
