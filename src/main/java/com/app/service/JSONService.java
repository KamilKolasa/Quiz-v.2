package com.app.service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Answer;
import com.app.model.Category;
import com.app.model.Level;
import com.app.model.Question;
import com.app.model.dto.AnswerDto;
import com.app.model.dto.CategoryDto;
import com.app.model.dto.QuestionDto;
import com.app.repository.AnswerRepository;
import com.app.repository.CategoryRepository;
import com.app.repository.QuestionRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JSONService {
    //@Value("${file.path}")
    //private String filePath;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionService questionService;
    private final CategoryService categoryService;

    public void getQuestions() {
        //try (FileReader reader = new FileReader(filePath + "fileQuestion.json")) {
        try (FileReader reader = new FileReader("fileQuestion.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            QuestionDto[] questions = gson.fromJson(reader, QuestionDto[].class);
            List<QuestionDto> questionDatabase = questionService.findAllQuestion();

            for (QuestionDto q : questions) {

                if (validation(q, questionDatabase)) {

                    Category category = categoryRepository.findByName(q.getCategory().getName()).orElse(null);
                    if (category == null) {
                        category = categoryRepository.save(ModelMapper.fromCategoryDtoToCategory(q.getCategory()));
                    }

                    Level level = q.getLevel();
                    Question questionBeforeInsert = ModelMapper.fromQuestionDtoToQuestion(q);
                    questionBeforeInsert.setLevel(level);
                    questionBeforeInsert.setCategory(category);
                    questionBeforeInsert.setFilename(q.getFilename());
                    Question question = questionRepository.save(questionBeforeInsert);

                    List<AnswerDto> answersDto = q.getAnswers();
                    for (AnswerDto a : answersDto) {
                        Answer aa = ModelMapper.fromAnswerDtoToAnswer(a);
                        aa.setQuestion(question);
                        if (a.getCorrect()) {
                            aa.setCorrect(true);
                        }
                        answerRepository.save(aa);
                    }
                }
            }
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET FILE QUESTION JSON");
        }
    }

    private boolean validation(QuestionDto question, List<QuestionDto> listQuestion) {
        if (listQuestion == null) {
            throw new MyException(ExceptionCode.SERVICE, "VALIDATION GET QUESTIONS JSON - LIST QUESTION IS NULL");
        }
        if (question == null) {
            return false;
        }
        String text = question.getText();
        for (QuestionDto q : listQuestion) {
            if (q.getText().equals(text)) {
                return !q.getAnswers()
                        .stream()
                        .filter(x -> x.getCorrect())
                        .findFirst().orElseThrow(() -> new MyException(ExceptionCode.VALIDATION, "FIND GOOD ANSWER WITH DATABASE"))
                        .getText()
                        .equals(question
                                .getAnswers()
                                .stream()
                                .filter(x -> x.getCorrect()).findFirst().orElseThrow(() -> new MyException(ExceptionCode.VALIDATION, "FIND GOOD ANSWER WITH FILE JSON"))
                                .getText());
            }
        }
        return true;
    }

    public void saveQuestions() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<QuestionDto> questions = Arrays.asList(
                    QuestionDto.builder()
                            .text("Co przedstawia zdjęcie?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Gwiezdny Niszczyciel typu Imperial").correct(true).build(),
                                    AnswerDto.builder().text("Gwiezdny Niszczyciel typu Victory").correct(false).build(),
                                    AnswerDto.builder().text("Gwiezdny Niszczyciel typu Imperious").correct(false).build(),
                                    AnswerDto.builder().text("Gwiezdny Niszczyciel typu Eclipse").correct(false).build())
                            )
                            .filename("pic1.jpg")
                            .build(),
                    QuestionDto.builder()
                            .text("Jaką maszyne kroczącą przedstawia obrazek?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("AT-AT").correct(true).build(),
                                    AnswerDto.builder().text("AT-ST").correct(false).build(),
                                    AnswerDto.builder().text("AT-RT").correct(false).build(),
                                    AnswerDto.builder().text("AT-DP").correct(false).build())
                            )
                            .filename("pic2.jpg")
                            .build(),
                    QuestionDto.builder()
                            .text("Z którego filmu jest to scena?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Mroczne widmo").correct(false).build(),
                                    AnswerDto.builder().text("Zemsta Sithów").correct(true).build(),
                                    AnswerDto.builder().text("Przebudzenie Mocy").correct(false).build(),
                                    AnswerDto.builder().text("Ostatni Jedi").correct(false).build())
                            )
                            .filename("pic3.jpg")
                            .build(),
                    QuestionDto.builder()
                            .text("Kim byli Sithowie?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("wyznawcy ciemnej strony mocy").correct(true).build(),
                                    AnswerDto.builder().text("wyznawcy jasnej strony mocy").correct(false).build(),
                                    AnswerDto.builder().text("każde zwierze obdarzone mocą").correct(false).build(),
                                    AnswerDto.builder().text("każda osoba obdarzona mocą").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Kim był Darth Vader?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Sithem i uczniem Palpatine").correct(true).build(),
                                    AnswerDto.builder().text("Sithem i mistrzem Palpatine").correct(false).build(),
                                    AnswerDto.builder().text("Przywódcą Imperium").correct(false).build(),
                                    AnswerDto.builder().text("Dowódcą Imperium").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Kto był głownym bochaterem filmy Rogue One?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Jyn Erso").correct(true).build(),
                                    AnswerDto.builder().text("Rey").correct(false).build(),
                                    AnswerDto.builder().text("Luke Skywalker").correct(false).build(),
                                    AnswerDto.builder().text("Han Solo").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Kto był senatorem planety Naboo?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.NORMAL)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Palpatine").correct(true).build(),
                                    AnswerDto.builder().text("Yuuzhan Vong").correct(false).build(),
                                    AnswerDto.builder().text("Tenebrous").correct(false).build(),
                                    AnswerDto.builder().text("Coleman Trebor").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Jakiej rasy był Darth Maul?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.NORMAL)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Zabrak").correct(true).build(),
                                    AnswerDto.builder().text("Yuuzhan Vong").correct(false).build(),
                                    AnswerDto.builder().text("Zygerrian").correct(false).build(),
                                    AnswerDto.builder().text("Nullanin").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Wskaż mistrzów Anakina Skywalkera?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.NORMAL)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Obi-Wan Kenobi i Darth Sidious").correct(true).build(),
                                    AnswerDto.builder().text("Obi-Wan Kenobi i Darth Vader").correct(false).build(),
                                    AnswerDto.builder().text("Darth Plagueis i Obi-Wan Kenobi").correct(false).build(),
                                    AnswerDto.builder().text("Qui-Gon Jinn i Darth Sidious").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Czym było AT-ST?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.NORMAL)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("dwunożna maszyna krocząca imperium").correct(true).build(),
                                    AnswerDto.builder().text("dwunożna maszyna krocząca klonów").correct(false).build(),
                                    AnswerDto.builder().text("czteronożna maszyna krocząca imperium").correct(false).build(),
                                    AnswerDto.builder().text("czteronożna maszyna krocząca klonów").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Czym była \"zasada dwóch\"?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.NORMAL)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("zawsze tylko 2 stihów uczeń i mistrz").correct(true).build(),
                                    AnswerDto.builder().text("rycerze jedi zawsze mieli we dwóch działać/walczyć").correct(false).build(),
                                    AnswerDto.builder().text("jeden rycerz jedi mogł mieć tylko 1 ucznia").correct(false).build(),
                                    AnswerDto.builder().text("dwóch sithów zawsze miało być razem").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Kto był mistrzem imperatora Palpatine?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.HARD)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Darth Plagueis").correct(true).build(),
                                    AnswerDto.builder().text("Darth Tenebrous").correct(false).build(),
                                    AnswerDto.builder().text("Darth Sidious").correct(false).build(),
                                    AnswerDto.builder().text("Darth Maul").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Który z jedi zginął podczas próby aresztowania Dartha Sidiousa?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.HARD)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Saesee Tiin").correct(true).build(),
                                    AnswerDto.builder().text("Yaddle").correct(false).build(),
                                    AnswerDto.builder().text("Yarael Poof").correct(false).build(),
                                    AnswerDto.builder().text("Coleman Trebor").correct(false).build())
                            ).filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Kto zginął podczas misji na malachor w serialu rebelianci?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.HARD)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("5, 8 i 7 brat").correct(false).build(),
                                    AnswerDto.builder().text("5 i 8 brat 7 siostra").correct(true).build(),
                                    AnswerDto.builder().text("4, 6 i 7 brat").correct(false).build(),
                                    AnswerDto.builder().text("4 i 6 brat 7 siostra").correct(false).build())
                            ).filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Kto stworzył zasade dwóch?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.HARD)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Tenebrous").correct(false).build(),
                                    AnswerDto.builder().text("Bane").correct(true).build(),
                                    AnswerDto.builder().text("Revan").correct(false).build(),
                                    AnswerDto.builder().text("Malak").correct(false).build())
                            ).filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Jakiego typu okrętu był sokół milenium?")
                            .category(CategoryDto.builder().name("Star Wars").build())
                            .level(Level.HARD)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("YT-1300").correct(true).build(),
                                    AnswerDto.builder().text("YT-2300").correct(false).build(),
                                    AnswerDto.builder().text("TY-1300").correct(false).build(),
                                    AnswerDto.builder().text("TY-2300").correct(false).build())
                            ).filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Jaki to karabin?")
                            .category(CategoryDto.builder().name("Militaria").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("AK-74").correct(true).build(),
                                    AnswerDto.builder().text("G36").correct(false).build(),
                                    AnswerDto.builder().text("M4").correct(false).build(),
                                    AnswerDto.builder().text("FN FAL").correct(false).build())
                            ).filename("pic4.jpg")
                            .build(),
                    QuestionDto.builder()
                            .text("Jaki amerykański karabin był samopowtaralny który po wystrzeleniu magazynka wydawał charakterystyczny \"brzdęk\"")
                            .category(CategoryDto.builder().name("Militaria").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("M1 Garand").correct(true).build(),
                                    AnswerDto.builder().text("M1 Carabine").correct(false).build(),
                                    AnswerDto.builder().text("M1903 Springfield").correct(false).build(),
                                    AnswerDto.builder().text("Winchester").correct(false).build())
                            ).filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Wskaż myśliwiec niemiecki?")
                            .category(CategoryDto.builder().name("Militaria").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("Messerschmitt Me 309").correct(true).build(),
                                    AnswerDto.builder().text("Reggiane Re.2003").correct(false).build(),
                                    AnswerDto.builder().text("ŁaGG-3").correct(false).build(),
                                    AnswerDto.builder().text("Seversky P-35").correct(false).build())
                            ).filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Jaki to czołg?")
                            .category(CategoryDto.builder().name("Militaria").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("T-34").correct(true).build(),
                                    AnswerDto.builder().text("T32").correct(false).build(),
                                    AnswerDto.builder().text("M4").correct(false).build(),
                                    AnswerDto.builder().text("pzkpfw IV").correct(false).build())
                            ).filename("pic5.jpg")
                            .build(),
                    QuestionDto.builder()
                            .text("Który to czołg amerykański?")
                            .category(CategoryDto.builder().name("Militaria").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("M1 Abrams").correct(true).build(),
                                    AnswerDto.builder().text("Leopard 2").correct(false).build(),
                                    AnswerDto.builder().text("Merkawa").correct(false).build(),
                                    AnswerDto.builder().text("T-72").correct(false).build())
                            ).filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Test Pytania 1?")
                            .category(CategoryDto.builder().name("KategoriaC1").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("odp11").correct(true).build(),
                                    AnswerDto.builder().text("odp12").correct(false).build(),
                                    AnswerDto.builder().text("odp13").correct(false).build(),
                                    AnswerDto.builder().text("odp14").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Test Pytania 2?")
                            .category(CategoryDto.builder().name("KategoriaC1").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("odp21").correct(true).build(),
                                    AnswerDto.builder().text("odp22").correct(false).build(),
                                    AnswerDto.builder().text("odp23").correct(false).build(),
                                    AnswerDto.builder().text("odp24").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Test Pytania 3?")
                            .category(CategoryDto.builder().name("KategoriaC1").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("odp31").correct(true).build(),
                                    AnswerDto.builder().text("odp32").correct(false).build(),
                                    AnswerDto.builder().text("odp33").correct(false).build(),
                                    AnswerDto.builder().text("odp34").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Test Pytania 4?")
                            .category(CategoryDto.builder().name("KategoriaC1").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("odp41").correct(true).build(),
                                    AnswerDto.builder().text("odp42").correct(false).build(),
                                    AnswerDto.builder().text("odp43").correct(false).build(),
                                    AnswerDto.builder().text("odp44").correct(false).build())
                            )
                            .filename("")
                            .build(),
                    QuestionDto.builder()
                            .text("Test Pytania 5?")
                            .category(CategoryDto.builder().name("KategoriaC1").build())
                            .level(Level.EASY)
                            .answers(Arrays.asList(
                                    AnswerDto.builder().text("odp51").correct(true).build(),
                                    AnswerDto.builder().text("odp52").correct(false).build(),
                                    AnswerDto.builder().text("odp53").correct(false).build(),
                                    AnswerDto.builder().text("odp54").correct(false).build())
                            )
                            .filename("")
                            .build()
            );

//            FileWriter writer = new FileWriter(filePath + "fileQuestion.json");
            FileWriter writer = new FileWriter("fileQuestion.json");
            gson.toJson(questions, writer);
            writer.close();
        } catch (Exception e) {
            throw new MyException(ExceptionCode.UNIDENTIFIED, "CREATE FILE JSON WITH QUESTION");
        }
    }

    public void addCategoryStandard(){
        Category category = categoryRepository.findByName("WSZYSTKO").orElse(null);
        if (category == null) {
            CategoryDto categoryDto = CategoryDto.builder().name("WSZYSTKO").build();
            categoryService.addCategory(categoryDto);
        }
    }
}
