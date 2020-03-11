package ir.beheshti.dandun.base.startup.user;

import ir.beheshti.dandun.base.startup.Insert;
import ir.beheshti.dandun.base.user.entity.*;
import ir.beheshti.dandun.base.user.repository.*;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import ir.beheshti.dandun.base.user.util.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PatientUser implements Insert {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private EssentialQuestionRepository essentialQuestionRepository;
    @Autowired
    private MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository;
    @Autowired
    private UserSingleQuestionAnswerRepository userSingleQuestionAnswerRepository;
    @Autowired
    private UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository;
    private void insertTestPatient() {
        UserEntity userEntity = new UserEntity();
        userEntity.setSignedUp(false);
        userEntity.setVerified(false);
        userEntity.setPassword("test");
        userEntity.setPhoneNumber("000000000000");
        userRepository.save(userEntity);

        PatientUserEntity patientUserEntity = new PatientUserEntity();
        patientUserEntity.setPatientId(userEntity.getId());
        patientUserEntity.setPatientStateType(PatientStateType.PENDING);
        patientRepository.save(patientUserEntity);

        List<EssentialQuestionEntity> questionEntityList = essentialQuestionRepository.findAllByQuestionOwnerTypeNot(QuestionOwnerType.Doctor);
        for (EssentialQuestionEntity questionEntity : questionEntityList) {
            if (questionEntity.getQuestionType().equals(QuestionType.SingleChoice)) {
                List<MultipleChoiceQuestionAnswerEntity> answer =
                        multipleChoiceQuestionAnswerRepository.findAllByEssentialQuestionId(questionEntity.getId());

                UserSingleQuestionAnswerEntity answerEntity = new UserSingleQuestionAnswerEntity();
                answerEntity.setUserId(userEntity.getId());
                answerEntity.setMultipleChoiceQuestionAnswerId(answer.get(0).getId());

                userSingleQuestionAnswerRepository.save(answerEntity);

            } else if (questionEntity.getQuestionType().equals(QuestionType.MultipleChoice)) {
                List<MultipleChoiceQuestionAnswerEntity> answer =
                        multipleChoiceQuestionAnswerRepository.findAllByEssentialQuestionId(questionEntity.getId());

                UserMultipleChoiceQuestionAnswerEntity firstAnswer = new UserMultipleChoiceQuestionAnswerEntity();
                firstAnswer.setUserId(userEntity.getId());
                firstAnswer.setMultipleChoiceQuestionAnswerId(answer.get(0).getId());

                UserMultipleChoiceQuestionAnswerEntity secondAnswer = new UserMultipleChoiceQuestionAnswerEntity();
                secondAnswer.setUserId(userEntity.getId());
                secondAnswer.setMultipleChoiceQuestionAnswerId(answer.get(1).getId());

                userMultipleQuestionAnswerRepository.saveAll(Arrays.asList(firstAnswer, secondAnswer));
            }
        }
    }

    @Override
    public void insert() {
        insertTestPatient();
    }
}
