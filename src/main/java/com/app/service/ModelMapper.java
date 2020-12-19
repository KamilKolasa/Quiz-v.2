package com.app.service;

import com.app.model.*;
import com.app.model.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public interface ModelMapper {

    static CategoryDto fromCategoryToCategoryDto(Category category) {
        return category == null ? null : CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    static Category fromCategoryDtoToCategory(CategoryDto categoryDto) {
        return categoryDto == null ? null : Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .questions(new HashSet<>())
                .build();
    }

    static AnswerDto fromAnswerToAnswerDto(Answer answer) {
        return answer == null ? null : AnswerDto.builder()
                .id(answer.getId())
                .correct(answer.isCorrect())
                .text(answer.getText())
                .questionId(answer.getQuestion().getId())
                .build();
    }

    static Answer fromAnswerDtoToAnswer(AnswerDto answerDto) {
        return answerDto == null ? null : Answer.builder()
                .id(answerDto.getId())
                .correct(answerDto.getCorrect())
                .text(answerDto.getText())
                .question(Question.builder().id(answerDto.getQuestionId()).build())
                .build();
    }

    private static int positionOfGoodAnswer(Set<Answer> answers) {
        if (answers == null) {
            return -1;
        }

        List<Answer> answers2 = new ArrayList<>(answers);
        for (int i = 0; i < answers2.size(); i++) {
            if (answers2.get(i).isCorrect()) {
                return i + 1;
            }
        }
        return -1;
    }

    static QuestionDto fromQuestionToQuestionDto(Question question) {
        return question == null ? null : QuestionDto.builder()
                .id(question.getId())
                .text(question.getText())
                .level(question.getLevel())
                .category(question.getCategory() == null ? null : fromCategoryToCategoryDto(question.getCategory()))
                .answers(question.getAnswers() == null ? new ArrayList<>() : question.getAnswers().stream().map(ModelMapper::fromAnswerToAnswerDto).collect(Collectors.toList()))
                .goodAnswer(positionOfGoodAnswer(question.getAnswers()))
                .filename(question.getFilename())
                .build();
    }

    static Question fromQuestionDtoToQuestion(QuestionDto questionDto) {
        return questionDto == null ? null : Question.builder()
                .id(questionDto.getId())
                .text(questionDto.getText())
                .level(questionDto.getLevel())
                .category(questionDto.getCategory() == null ? null : fromCategoryDtoToCategory(questionDto.getCategory()))
                .answers(questionDto.getAnswers() == null ? new HashSet<>() : questionDto.getAnswers().stream().map(ModelMapper::fromAnswerDtoToAnswer).collect(Collectors.toSet()))
                .filename(questionDto.getFilename())
                .build();
    }

    static QuizDto fromQuizToQuizDto(Quiz quiz) {
        return quiz == null ? null : QuizDto.builder()
                .id(quiz.getId())
                .user(fromUserToUserDto(quiz.getUser()))
                .date(quiz.getDate())
                .result(quiz.getResult())
                .goodAnswer(quiz.getGoodAnswer())
                .build();
    }

    static Quiz fromQuizDtoToQuiz(QuizDto quizDto) {
        return quizDto == null ? null : Quiz.builder()
                .id(quizDto.getId())
                .user(fromUserDtoToUser(quizDto.getUser()))
                .date(quizDto.getDate())
                .result(quizDto.getResult())
                .goodAnswer(quizDto.getGoodAnswer())
                .build();
    }

    static GameDto fromGameToGameDto(Game game) {
        return game == null ? null : GameDto.builder()
                .id(game.getId())
                .question(game.getQuestion() == null ? null : fromQuestionToQuestionDto(game.getQuestion()))
                .quiz(game.getQuiz() == null ? null : fromQuizToQuizDto(game.getQuiz()))
                .build();
    }

    static Game fromGameDtoToGame(GameDto gameDto) {
        return gameDto == null ? null : Game.builder()
                .id(gameDto.getId())
                .question(gameDto.getQuestion() == null ? null : fromQuestionDtoToQuestion(gameDto.getQuestion()))
                .quiz(gameDto.getQuiz() == null ? null : fromQuizDtoToQuiz(gameDto.getQuiz()))
                .build();
    }

    static UserDto fromUserToUserDto(User user) {
        return user == null ? null : UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .city(user.getCity())
                .bestResult(user.getBestResult())
                .build();
    }

    static User fromUserDtoToUser(UserDto userDto) {
        return userDto == null ? null : User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .age(userDto.getAge())
                .city(userDto.getCity())
                .quizzes(new HashSet<>())
                .bestResult(userDto.getBestResult())
                .build();
    }
}
