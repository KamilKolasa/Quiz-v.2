package com.app.service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Answer;
import com.app.model.Category;
import com.app.model.Level;
import com.app.model.Question;
import com.app.model.dto.QuestionDto;
import com.app.model.dto.QuestionFilterDto;
import com.app.repository.AnswerRepository;
import com.app.repository.CategoryRepository;
import com.app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerRepository answerRepository;
    private final FileService fileService;

    public QuestionDto addQuestion(QuestionDto question) {
        if (question == null) {
            throw new MyException(ExceptionCode.SERVICE, "ADD QUESTION - QUESTION IS NULL");
        }
        for (int i = 0; i < question.getAnswers().size(); i++) {
            if (question.getGoodAnswer().equals(i)) {
                question.getAnswers().get(i).setCorrect(true);
            } else {
                question.getAnswers().get(i).setCorrect(false);
            }
        }
        Category category = categoryRepository.findById(question.getCategory().getId()).orElseThrow(NullPointerException::new);
        Level level = question.getLevel();
        Question q = ModelMapper.fromQuestionDtoToQuestion(question);
        q.setCategory(category);
        q.setLevel(level);
        q.setFilename(fileService.addFile(question.getMultipartFile()));
        q.setAnswers(new HashSet<>());
        return ModelMapper.fromQuestionToQuestionDto(questionRepository.save(q));

    }

    public void updateQuestion(QuestionDto question) {
        if (question == null) {
            throw new MyException(ExceptionCode.SERVICE, "UPDATE QUESTION - QUESTION IS NULL");
        }
        // zajmujemy sie aktualizacja odpowiedzi
        int correctAnswer = question.getGoodAnswer();
        for (int i = 0; i < question.getAnswers().size(); i++) {
            Answer answer = answerRepository.findById(question.getAnswers().get(i).getId()).orElseThrow(NullPointerException::new);
            answer.setText(question.getAnswers().get(i).getText());
            if (i + 1 == correctAnswer) {
                answer.setCorrect(true);
            } else {
                answer.setCorrect(false);
            }
            answerRepository.save(answer);
        }
        Category category = categoryRepository.findById(question.getCategory().getId()).orElseThrow(NullPointerException::new);
        Level level = question.getLevel();
        Question q = questionRepository.findById(question.getId()).orElseThrow(NullPointerException::new);
        q.setLevel(level);
        q.setCategory(category);
        q.setFilename(fileService.updateFile(question.getMultipartFile(), question.getFilename()));
        q.setText(question.getText());
        questionRepository.save(q);

    }

    public QuestionDto deleteQuestion(Long id) {
        if (id == null) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE QUESTION - ID IS NULL");
        }
        Set<Answer> answers = answerRepository.findAll().stream().filter(a -> a.getQuestion().getId().equals(id)).collect(Collectors.toSet());
        answers.forEach(a -> answerRepository.delete(a));
        fileService.removeFile(findOneQuestion(id).get().getFilename());
        Question question = questionRepository.findById(id).orElseThrow(() -> new NullPointerException("NOT FOUND QUESTION"));
        questionRepository.delete(question);
        return ModelMapper.fromQuestionToQuestionDto(question);
    }

    public Optional<QuestionDto> findOneQuestion(Long id) {
        if (id == null) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE QUESTION - ID IS NULL");
        }
        return questionRepository.findById(id).map(p -> ModelMapper.fromQuestionToQuestionDto(p));
    }

    public List<QuestionDto> findAllQuestion() {
        return questionRepository
                .findAll()
                .stream()
                .map(p -> ModelMapper.fromQuestionToQuestionDto(p))
                .collect(Collectors.toList());
    }

    public List<QuestionDto> findAllQuestionAfterFiltrs(QuestionFilterDto questionFilterDto) {
        if (questionFilterDto == null) {
            throw new MyException(ExceptionCode.SERVICE, "FIND ALL QUESTION AFTER FILTRS - QUESTION FILTER IS NULL");
        }
        Category category = categoryRepository.findById(questionFilterDto.getCategoryId()).orElseThrow(NullPointerException::new);
        Level level = questionFilterDto.getLevel();


        List<QuestionDto> filteredByCategory = questionRepository
                .findAll()
                .stream()
                .map(ModelMapper::fromQuestionToQuestionDto)
                .collect(Collectors.toList());
        if (!category.getName().equals("WSZYSTKO")) {
            filteredByCategory = filteredByCategory
                    .stream()
                    .filter(q -> q.getCategory().getName().equals(category.getName()))
                    .collect(Collectors.toList());
        }

        List<QuestionDto> filteredByLevel = questionRepository
                .findAll()
                .stream()
                .map(ModelMapper::fromQuestionToQuestionDto)
                .collect(Collectors.toList());
        if (!level.getName().equals("WSZYSTKO")) {
            filteredByLevel = filteredByLevel
                    .stream()
                    .filter(q -> q.getLevel().equals(level))
                    .collect(Collectors.toList());
        }

        filteredByCategory.retainAll(filteredByLevel);
        return filteredByCategory;
    }

    public List<QuestionDto> random5question(List<QuestionDto> listQuestion) {
        if (listQuestion == null) {
            throw new MyException(ExceptionCode.SERVICE, "FIND RADNOM 5 QUESTION - LIST QUESTIONS IS NULL");
        }

        if (listQuestion.size() == 0) {
            throw new MyException(ExceptionCode.SERVICE, "FIND RADNOM 5 QUESTION - LIST QUESTIONS IS 0");
        }

        Random rnd = new Random();
        Set<Integer> set = new HashSet<>();
        do {
            set.add(rnd.nextInt(listQuestion.size()));
        } while (set.size() < 5);

        List<QuestionDto> result = new ArrayList<>();
        for (Integer i : set) {
            result.add(listQuestion.get(i));
        }
        return result;
    }
}
