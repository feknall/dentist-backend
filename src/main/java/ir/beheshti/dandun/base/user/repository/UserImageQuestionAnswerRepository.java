package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.UserImageQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.entity.UserRangeQuestionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserImageQuestionAnswerRepository extends JpaRepository<UserImageQuestionAnswerEntity, Integer> {

    boolean existsByUserIdAndEssentialQuestionId(int userId, int questionId);
    List<UserImageQuestionAnswerEntity> findAllByUserIdAndEssentialQuestionId(int userId, int questionId);
    Optional<UserImageQuestionAnswerEntity> findByIdAndUserId(int imageId, int userId);
    void deleteAllByUserId(int userId);
}
