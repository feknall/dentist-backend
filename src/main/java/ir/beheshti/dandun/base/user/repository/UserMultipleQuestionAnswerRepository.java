package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.UserMultipleChoiceQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.entity.UserOpenQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMultipleQuestionAnswerRepository extends JpaRepository<UserMultipleChoiceQuestionAnswerEntity, Integer> {

    List<UserMultipleChoiceQuestionAnswerEntity> findByUserIdAndMultipleChoiceQuestionAnswerIdIsIn(int userId, Iterable<Integer> answerIds);
    boolean existsByUserIdAndMultipleChoiceQuestionAnswerIdIsIn(int userId, Iterable<Integer> answerIds);
    void deleteAllByUserId(int userId);
}
