package com.app.service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Answer;
import com.app.model.Question;
import com.app.model.dto.AnswerDto;
import com.app.model.dto.QuestionDto;
import com.app.repository.AnswerRepository;
import com.app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    public AnswerDto addAnswer(AnswerDto answer) {
        if (answer == null) {
            throw new MyException(ExceptionCode.SERVICE, "ADD ANSWER - ANSWER IS NULL");
        }
        Answer a = ModelMapper.fromAnswerDtoToAnswer(answer);
        Question q = questionRepository.findById(answer.getQuestionId()).orElseThrow(NullPointerException::new);
        a.setQuestion(q);
        return ModelMapper.fromAnswerToAnswerDto(answerRepository.save(a));
    }

    public void addListAnswers(QuestionDto question, Long questionId) {
        if (question == null) {
            throw new MyException(ExceptionCode.SERVICE, "ADD ANSWERS LIST - QUESTION IS NULL");
        }
        if (question.getAnswers() == null) {
            throw new MyException(ExceptionCode.SERVICE, "ADD ANSWERS LIST - ANSWERS LIST IS NULL");
        }
        if (questionId == null) {
            throw new MyException(ExceptionCode.SERVICE, "ADD ANSWERS LIST - QUESTION ID IS NULL");
        }
        for (AnswerDto answerDto : question.getAnswers()) {
            answerDto.setQuestionId(questionId);
            addAnswer(answerDto);
        }
    }

    public AnswerDto findCorrectAnswer(Long idQuestion) {
        if (idQuestion == null) {
            throw new MyException(ExceptionCode.SERVICE, "FIND ONE CORRECT ANSWER - ID QUESTION IS NULL");
        }
        List<AnswerDto> answers = questionService.findOneQuestion(idQuestion).get().getAnswers();
        for (AnswerDto a : answers) {
            if (a.getCorrect()) {
                return a;
            }
        }
        return null;
    }
}
