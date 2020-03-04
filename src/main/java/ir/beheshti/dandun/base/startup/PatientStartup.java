package ir.beheshti.dandun.base.startup;

import ir.beheshti.dandun.base.user.entity.EssentialQuestionEntity;
import ir.beheshti.dandun.base.user.entity.MultipleChoiceQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.repository.EssentialQuestionRepository;
import ir.beheshti.dandun.base.user.repository.MultipleChoiceQuestionAnswerRepository;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import ir.beheshti.dandun.base.user.util.QuestionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PatientStartup implements Insert {

    private final EssentialQuestionRepository essentialQuestionRepository;
    private final MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository;
    private final UserRepository userRepository;

    @Value("${dandun.questions.insert}")
    private Boolean insertQuestions;

    public PatientStartup(EssentialQuestionRepository essentialQuestionRepository, MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository, UserRepository userRepository) {
        this.essentialQuestionRepository = essentialQuestionRepository;
        this.multipleChoiceQuestionAnswerRepository = multipleChoiceQuestionAnswerRepository;
        this.userRepository = userRepository;
    }

    private void insertPatientQuestions() {
        insertSalamati();
        insertBarrasi();
        insertDandun();
        insertBimari();
        insertDaru();
        insertSigar();
        insertAks();
    }

    private void insertAks() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setQuestionOwnerType(QuestionOwnerType.Patient);
        entity.setDescription("لطفا تصاویر مرتبط را بارگذاری کنید:");
        entity.setQuestionType(QuestionType.Image);
        essentialQuestionRepository.save(entity);
    }

    private void insertBarrasi() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setDescription("خواستار بررسی وضعیت سلامت دهان و دندان‌هایم هستم:");
        entity.setQuestionType(QuestionType.SingleChoice);
        entity.setQuestionOwnerType(QuestionOwnerType.Patient);
        essentialQuestionRepository.save(entity);

        MultipleChoiceQuestionAnswerEntity yesAnswer = new MultipleChoiceQuestionAnswerEntity();
        yesAnswer.setDescription("بله");
        yesAnswer.setEssentialQuestionId(entity.getId());
        MultipleChoiceQuestionAnswerEntity noAnswer = new MultipleChoiceQuestionAnswerEntity();
        noAnswer.setDescription("خیر");
        noAnswer.setEssentialQuestionId(entity.getId());
        multipleChoiceQuestionAnswerRepository.saveAll(Arrays.asList(yesAnswer, noAnswer));
    }

    private void insertSalamati() {
        EssentialQuestionEntity salamati = new EssentialQuestionEntity();
        salamati.setDescription("لطفا وضعیت سلامت خود را ارزیابی نمایید:");
        salamati.setQuestionType(QuestionType.SingleChoice);
        salamati.setQuestionOwnerType(QuestionOwnerType.Patient);
        essentialQuestionRepository.save(salamati);

        MultipleChoiceQuestionAnswerEntity answer1 = new MultipleChoiceQuestionAnswerEntity();
        answer1.setDescription("کاملا سالم");
        answer1.setEssentialQuestionId(salamati.getId());
        MultipleChoiceQuestionAnswerEntity answer2 = new MultipleChoiceQuestionAnswerEntity();
        answer2.setDescription("سابقه‌ی بیماری");
        answer2.setEssentialQuestionId(salamati.getId());
        MultipleChoiceQuestionAnswerEntity answer3 = new MultipleChoiceQuestionAnswerEntity();
        answer3.setDescription("نمیدانم");
        answer3.setEssentialQuestionId(salamati.getId());
        multipleChoiceQuestionAnswerRepository.saveAll(Arrays.asList(answer1, answer2, answer3));

        EssentialQuestionEntity tahteNazar = new EssentialQuestionEntity();
        tahteNazar.setDescription("آیا تحت نظر پزشک هستید؟");
        tahteNazar.setQuestionType(QuestionType.SingleChoice);
        tahteNazar.setQuestionOwnerType(QuestionOwnerType.Patient);
        tahteNazar.setDependOnAnswerId(answer2.getId());
        essentialQuestionRepository.save(tahteNazar);

        MultipleChoiceQuestionAnswerEntity are = new MultipleChoiceQuestionAnswerEntity();
        are.setDescription("بله");
        are.setEssentialQuestionId(tahteNazar.getId());
        MultipleChoiceQuestionAnswerEntity na = new MultipleChoiceQuestionAnswerEntity();
        na.setDescription("خیر");
        na.setEssentialQuestionId(tahteNazar.getId());
        multipleChoiceQuestionAnswerRepository.saveAll(Arrays.asList(are, na));
    }

    private void insertDandun() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setDescription("شکایت اصلی شما چیست؟ و خواستار چه درمانی می‌باشید؟");
        entity.setQuestionType(QuestionType.MultipleChoice);
        entity.setQuestionOwnerType(QuestionOwnerType.Patient);
        essentialQuestionRepository.save(entity);

        MultipleChoiceQuestionAnswerEntity answerEntity1 = new MultipleChoiceQuestionAnswerEntity();
        answerEntity1.setDescription("بدون مشکل خاص");
        answerEntity1.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity answerEntity2 = new MultipleChoiceQuestionAnswerEntity();
        answerEntity2.setDescription("درد دندان");
        answerEntity2.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity answerEntity3 = new MultipleChoiceQuestionAnswerEntity();
        answerEntity3.setDescription("جایگذاری ایمپلنت");
        answerEntity3.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity answerEntity4 = new MultipleChoiceQuestionAnswerEntity();
        answerEntity4.setDescription("پروتز دندان");
        answerEntity4.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity answerEntity5 = new MultipleChoiceQuestionAnswerEntity();
        answerEntity5.setDescription("درمان عصب دندان");
        answerEntity5.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity answerEntity6 = new MultipleChoiceQuestionAnswerEntity();
        answerEntity6.setDescription("پرکردگی");
        answerEntity6.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity answerEntity7 = new MultipleChoiceQuestionAnswerEntity();
        answerEntity7.setDescription("سایر موارد");
        answerEntity7.setEssentialQuestionId(entity.getId());

        multipleChoiceQuestionAnswerRepository.saveAll(Arrays.asList(answerEntity1, answerEntity2, answerEntity3,
                answerEntity4, answerEntity5, answerEntity6, answerEntity7));
    }

    private void insertBimari() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setDescription("سابقه‌ی بیماری خاصی دارید؟");
        entity.setQuestionType(QuestionType.MultipleChoice);
        entity.setQuestionOwnerType(QuestionOwnerType.Patient);
        essentialQuestionRepository.save(entity);

        MultipleChoiceQuestionAnswerEntity ans1 = new MultipleChoiceQuestionAnswerEntity();
        ans1.setDescription("قلبی");
        ans1.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans2 = new MultipleChoiceQuestionAnswerEntity();
        ans2.setDescription("دیابت");
        ans2.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans3 = new MultipleChoiceQuestionAnswerEntity();
        ans3.setDescription("تنفسی");
        ans3.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans4 = new MultipleChoiceQuestionAnswerEntity();
        ans4.setDescription("خونریزی غیرطبیعی");
        ans4.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans5 = new MultipleChoiceQuestionAnswerEntity();
        ans5.setDescription("کلیوی");
        ans5.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans6 = new MultipleChoiceQuestionAnswerEntity();
        ans6.setDescription("تیروئید");
        ans6.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans7 = new MultipleChoiceQuestionAnswerEntity();
        ans7.setDescription("صرع");
        ans7.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans8 = new MultipleChoiceQuestionAnswerEntity();
        ans8.setDescription("بارداری");
        ans8.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans9 = new MultipleChoiceQuestionAnswerEntity();
        ans9.setDescription("دوران شیردهی");
        ans9.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans10 = new MultipleChoiceQuestionAnswerEntity();
        ans10.setDescription("فشار خون");
        ans10.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans11 = new MultipleChoiceQuestionAnswerEntity();
        ans11.setDescription("بیماری خونی");
        ans11.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans12 = new MultipleChoiceQuestionAnswerEntity();
        ans12.setDescription("هپاتیت");
        ans12.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans13 = new MultipleChoiceQuestionAnswerEntity();
        ans13.setDescription("آلرژی");
        ans13.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans14 = new MultipleChoiceQuestionAnswerEntity();
        ans14.setDescription("گوارشی");
        ans14.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity ans15 = new MultipleChoiceQuestionAnswerEntity();
        ans15.setDescription("سایر موارد");
        ans15.setEssentialQuestionId(entity.getId());

        multipleChoiceQuestionAnswerRepository.saveAll(Arrays.asList(ans1, ans2, ans3, ans4, ans5,
                ans6, ans7, ans8, ans9, ans10, ans11, ans12, ans13, ans14, ans15));
    }

    private void insertDaru() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setDescription("آیا سابقه‌ی مصرف داروی خاصی دارید؟");
        entity.setQuestionType(QuestionType.MultipleChoice);
        entity.setQuestionOwnerType(QuestionOwnerType.Patient);
        essentialQuestionRepository.save(entity);

        MultipleChoiceQuestionAnswerEntity daru1 = new MultipleChoiceQuestionAnswerEntity();
        daru1.setDescription("آنتی‌بیوتیک");
        daru1.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru2 = new MultipleChoiceQuestionAnswerEntity();
        daru2.setDescription("مکمل‌ها");
        daru2.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru3 = new MultipleChoiceQuestionAnswerEntity();
        daru3.setDescription("داروهای قلبی");
        daru3.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru4 = new MultipleChoiceQuestionAnswerEntity();
        daru4.setDescription("ضد فشارخون");
        daru4.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru5 = new MultipleChoiceQuestionAnswerEntity();
        daru5.setDescription("انسولین");
        daru5.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru6 = new MultipleChoiceQuestionAnswerEntity();
        daru6.setDescription("آرام‌بخش‌ها");
        daru6.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru7 = new MultipleChoiceQuestionAnswerEntity();
        daru7.setDescription("کورتیکواستروییدها");
        daru7.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru8 = new MultipleChoiceQuestionAnswerEntity();
        daru8.setDescription("ضد حساسیت‌ها");
        daru8.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru9 = new MultipleChoiceQuestionAnswerEntity();
        daru9.setDescription("روانگردان‌");
        daru9.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru10 = new MultipleChoiceQuestionAnswerEntity();
        daru10.setDescription("ضد انعتقاد");
        daru10.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru11 = new MultipleChoiceQuestionAnswerEntity();
        daru11.setDescription("آسپرین");
        daru11.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity daru12 = new MultipleChoiceQuestionAnswerEntity();
        daru12.setDescription("سایر داروها");
        daru12.setEssentialQuestionId(entity.getId());

        multipleChoiceQuestionAnswerRepository.saveAll(Arrays.asList(daru1, daru2, daru3, daru4, daru5, daru6, daru7, daru8, daru9,
                daru10, daru11, daru12));
    }

    private void insertSigar() {
        EssentialQuestionEntity entity = new EssentialQuestionEntity();
        entity.setDescription("آیا سیگار می‌کشید؟");
        entity.setQuestionType(QuestionType.SingleChoice);
        entity.setQuestionOwnerType(QuestionOwnerType.Patient);
        essentialQuestionRepository.save(entity);

        MultipleChoiceQuestionAnswerEntity sigarYes = new MultipleChoiceQuestionAnswerEntity();
        sigarYes.setDescription("بله");
        sigarYes.setEssentialQuestionId(entity.getId());

        MultipleChoiceQuestionAnswerEntity sigarNo = new MultipleChoiceQuestionAnswerEntity();
        sigarNo.setDescription("خیر");
        sigarNo.setEssentialQuestionId(entity.getId());

        multipleChoiceQuestionAnswerRepository.saveAll(Arrays.asList(sigarYes, sigarNo));
    }

    @Override
    public void insert() {
        insertPatientQuestions();
    }
}
