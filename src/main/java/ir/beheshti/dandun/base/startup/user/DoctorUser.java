package ir.beheshti.dandun.base.startup.user;

import ir.beheshti.dandun.base.startup.Insert;
import ir.beheshti.dandun.base.user.entity.*;
import ir.beheshti.dandun.base.user.repository.*;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import ir.beheshti.dandun.base.user.util.QuestionType;
import ir.beheshti.dandun.base.user.util.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DoctorUser implements Insert {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private EssentialQuestionRepository essentialQuestionRepository;
    @Autowired
    private MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository;
    @Autowired
    private UserSingleQuestionAnswerRepository userSingleQuestionAnswerRepository;
    @Autowired
    private UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository;

    private void insertTestDoctor() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("علی");
        userEntity.setLastName("دکیان");
        userEntity.setSignedUp(true);
        userEntity.setVerified(true);
        userEntity.setPassword("test");
        userEntity.setPhoneNumber("090000000001");
        userEntity.setUserType(UserType.Doctor);
        userEntity.setNotificationToken("token");
        userRepository.save(userEntity);

        DoctorUserEntity doctorUserEntity = new DoctorUserEntity();
        doctorUserEntity.setDoctorId(userEntity.getId());
        doctorUserEntity.setDoctorStateType(DoctorStateType.ACTIVE);
        doctorRepository.save(doctorUserEntity);

        List<EssentialQuestionEntity> questionEntityList = essentialQuestionRepository.findAllByQuestionOwnerTypeNot(QuestionOwnerType.Patient);
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
        insertTestDoctor();
    }
}
