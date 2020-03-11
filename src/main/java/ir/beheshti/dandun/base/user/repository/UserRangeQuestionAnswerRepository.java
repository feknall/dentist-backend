package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.UserRangeQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.entity.UserSingleQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRangeQuestionAnswerRepository extends JpaRepository<UserRangeQuestionAnswerEntity, Integer> {

    void deleteAllByUserId(int userId);

    boolean existsByUserIdAndEssentialQuestionId(int userId, int questionId);
}
