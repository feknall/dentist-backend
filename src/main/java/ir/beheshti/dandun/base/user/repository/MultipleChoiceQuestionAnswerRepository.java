package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.MultipleChoiceQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MultipleChoiceQuestionAnswerRepository extends JpaRepository<MultipleChoiceQuestionAnswerEntity, Integer> {

    @Override
    List<MultipleChoiceQuestionAnswerEntity> findAllById(Iterable<Integer> integers);

    List<MultipleChoiceQuestionAnswerEntity> findAllByEssentialQuestionId(int questionId);

    boolean existsById(int answerId);

    int countByIdInAndEssentialQuestionEntityId(Iterable<Integer> ids, int questionId);
}
