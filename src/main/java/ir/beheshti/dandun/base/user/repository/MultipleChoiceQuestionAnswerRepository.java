package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.MultipleChoiceQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultipleChoiceQuestionAnswerRepository extends JpaRepository<MultipleChoiceQuestionAnswerEntity, Integer> {

    @Override
    List<MultipleChoiceQuestionAnswerEntity> findAllById(Iterable<Integer> integers);

    List<MultipleChoiceQuestionAnswerEntity> findAllByIdAndEssentialQuestionId(Iterable<Integer> ids, int questionId);
    int countByIdAndEssentialQuestionIdIn(Iterable<Integer> ids, int questionId);
}
