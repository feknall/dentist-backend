package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.UserOpenQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOpenQuestionAnswerRepository extends JpaRepository<UserOpenQuestionAnswerEntity, Integer> {

    Optional<UserOpenQuestionAnswerEntity> findByUserIdAndEssentialQuestionId(int userId, int questionId);

    void deleteAllByUserId(int userId);
}
