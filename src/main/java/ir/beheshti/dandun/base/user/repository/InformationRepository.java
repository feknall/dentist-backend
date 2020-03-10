package ir.beheshti.dandun.base.user.repository;

import ir.beheshti.dandun.base.user.entity.EssentialQuestionEntity;
import ir.beheshti.dandun.base.user.entity.InformationEntity;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InformationRepository extends JpaRepository<InformationEntity, Integer> {

}
