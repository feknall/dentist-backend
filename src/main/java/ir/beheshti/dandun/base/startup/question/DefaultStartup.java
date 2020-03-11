package ir.beheshti.dandun.base.startup.question;

import ir.beheshti.dandun.base.startup.Insert;
import ir.beheshti.dandun.base.user.entity.EssentialQuestionEntity;
import ir.beheshti.dandun.base.user.entity.MultipleChoiceQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.repository.EssentialQuestionRepository;
import ir.beheshti.dandun.base.user.repository.MultipleChoiceQuestionAnswerRepository;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import ir.beheshti.dandun.base.user.util.QuestionType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DefaultStartup implements Insert {

    private final EssentialQuestionRepository essentialQuestionRepository;
    private final MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository;

    public DefaultStartup(EssentialQuestionRepository essentialQuestionRepository, MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository) {
        this.essentialQuestionRepository = essentialQuestionRepository;
        this.multipleChoiceQuestionAnswerRepository = multipleChoiceQuestionAnswerRepository;
    }

    private void insertPublicQuestions() {
        insertSen();
        insertJensiat();
    }

    private void insertSen() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setQuestionOwnerType(QuestionOwnerType.Public);
        entity.setDescription("سن:");
        entity.setQuestionType(QuestionType.Range);
        essentialQuestionRepository.save(entity);
    }

    private void insertJensiat() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setQuestionOwnerType(QuestionOwnerType.Public);
        entity.setDescription("جنسیت:");
        entity.setQuestionType(QuestionType.SingleChoice);
        essentialQuestionRepository.save(entity);

        MultipleChoiceQuestionAnswerEntity answer1 = new MultipleChoiceQuestionAnswerEntity();
        answer1.setDescription("مرد");
        answer1.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity answer2 = new MultipleChoiceQuestionAnswerEntity();
        answer2.setDescription("زن");
        answer2.setEssentialQuestionId(entity.getId());
        multipleChoiceQuestionAnswerRepository.saveAll(Arrays.asList(answer1, answer2));
    }

    @Override
    public void insert() {
        insertPublicQuestions();
    }
}
