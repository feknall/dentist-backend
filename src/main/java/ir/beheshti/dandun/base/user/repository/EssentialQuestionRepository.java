package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.EssentialQuestionEntity;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EssentialQuestionRepository extends JpaRepository<EssentialQuestionEntity, Integer> {

    List<EssentialQuestionEntity> findAllByQuestionOwnerTypeNot(QuestionOwnerType questionOwnerType);
    List<EssentialQuestionEntity> findAllByQuestionOwnerTypeEqualsOrQuestionOwnerTypeEquals(QuestionOwnerType firstOwner, QuestionOwnerType secondOwner);
    int countByIdIn(Iterable<Integer> ids);

}
