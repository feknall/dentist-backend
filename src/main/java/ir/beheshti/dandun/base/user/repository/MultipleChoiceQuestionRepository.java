package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.MultipleChoiceQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceQuestionRepository extends JpaRepository<Integer, MultipleChoiceQuestionEntity> {
}
