package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.MultipleChoiceQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceQuestionAnswerRepository extends JpaRepository<MultipleChoiceQuestionAnswerEntity, Integer> {
}
