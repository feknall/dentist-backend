package ir.beheshti.dandun.base.startup;

import ir.beheshti.dandun.base.user.entity.EssentialQuestionEntity;
import ir.beheshti.dandun.base.user.entity.MultipleChoiceQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.repository.EssentialQuestionRepository;
import ir.beheshti.dandun.base.user.repository.MultipleChoiceQuestionAnswerRepository;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import ir.beheshti.dandun.base.user.util.QuestionType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DoctorStartup implements Insert {

    private final EssentialQuestionRepository essentialQuestionRepository;
    private final MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository;

    public DoctorStartup(EssentialQuestionRepository essentialQuestionRepository, MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository) {
        this.essentialQuestionRepository = essentialQuestionRepository;
        this.multipleChoiceQuestionAnswerRepository = multipleChoiceQuestionAnswerRepository;
    }

    private void insertModateFaaliat() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setDescription("مدت فعالیت شما:");
        entity.setQuestionType(QuestionType.Range);
        entity.setQuestionOwnerType(QuestionOwnerType.Doctor);
        essentialQuestionRepository.save(entity);
    }

    private void insertAddressMatab() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setDescription("لطفا آدرس مطب خود را وارد کنید:");
        entity.setQuestionType(QuestionType.Open);
        entity.setQuestionOwnerType(QuestionOwnerType.Doctor);
        essentialQuestionRepository.save(entity);
    }

    private void insertTelephone() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setDescription("لطفا تلفن مطب را وارد کنید:");
        entity.setQuestionType(QuestionType.OpenNumber);
        entity.setQuestionOwnerType(QuestionOwnerType.Doctor);
        essentialQuestionRepository.save(entity);
    }

    private void insertZamineFaaliat() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setDescription("لطفا زمینه فعالیت خود را مشخص کنید:");
        entity.setQuestionType(QuestionType.MultipleChoice);
        entity.setQuestionOwnerType(QuestionOwnerType.Doctor);
        essentialQuestionRepository.save(entity);

        MultipleChoiceQuestionAnswerEntity ans1 = new MultipleChoiceQuestionAnswerEntity();
        ans1.setDescription("ترمیم و زیبایی");
        ans1.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity ans2 = new MultipleChoiceQuestionAnswerEntity();
        ans2.setDescription("ارتودنسی");
        ans2.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity ans3 = new MultipleChoiceQuestionAnswerEntity();
        ans3.setDescription("پروتز");
        ans3.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity ans4 = new MultipleChoiceQuestionAnswerEntity();
        ans4.setDescription("عمومی");
        ans4.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity ans5 = new MultipleChoiceQuestionAnswerEntity();
        ans5.setDescription("اطفال");
        ans5.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity ans6 = new MultipleChoiceQuestionAnswerEntity();
        ans6.setDescription("جراحی");
        ans6.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity ans7 = new MultipleChoiceQuestionAnswerEntity();
        ans7.setDescription("ایمپلنت");
        ans7.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity ans8 = new MultipleChoiceQuestionAnswerEntity();
        ans8.setDescription("اندو");
        ans8.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity ans9 = new MultipleChoiceQuestionAnswerEntity();
        ans9.setDescription("سایر موارد");
        ans9.setEssentialQuestionId(entity.getId());
        multipleChoiceQuestionAnswerRepository.saveAll(Arrays.asList(ans1, ans2, ans3, ans4,
                ans5, ans6, ans7, ans8, ans9));
    }

    @Override
    public void insert() {
        insertZamineFaaliat();
        insertModateFaaliat();
        insertAddressMatab();
        insertTelephone();
    }
}
