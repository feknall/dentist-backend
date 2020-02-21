package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.UserTrueFalseQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTrueFalseQuestionAnswerRepository extends JpaRepository<UserTrueFalseQuestionAnswerEntity, Integer> {

    boolean existsByUserIdAndEssentialQuestionId(int userId, int questionId);
}
