package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.UserSingleQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSingleQuestionAnswerRepository extends JpaRepository<UserSingleQuestionAnswerEntity, Integer> {

    boolean existsByUserIdAndMultipleChoiceQuestionAnswerId(int userId, int answerId);

    void deleteAllByUserId(int userId);
}
