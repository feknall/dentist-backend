package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.UserOpenNumberQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOpenNumberQuestionAnswerRepository extends JpaRepository<UserOpenNumberQuestionAnswerEntity, Integer> {

    boolean existsByUserIdAndEssentialQuestionId(int userId, int questionId);
    void deleteAllByUserId(int userId);
}
