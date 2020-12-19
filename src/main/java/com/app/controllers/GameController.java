package com.app.controllers;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.model.dto.*;
import com.app.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final UserService userService;
    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final QuizService quizService;
    private final GameService gameService;

    @GetMapping("/insert_user")
    public String userInsertGet(Model model) {
        model.addAttribute("user", new UserDto());
        return "game/insert_user";
    }

    @PostMapping("/insert_user")
    public String userInsertPost(@ModelAttribute UserDto userDto) {
        userService.addUser(userDto);
        return "redirect:/game/table_user";
    }

    @PostMapping("/delete_user")
    public String userDeletePost(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/game/table_user";
    }

    @GetMapping("/history_user/{id}")
    public String userHistoryGet(Model model, @PathVariable Long id) {
        model.addAttribute("history", quizService.findQuizOneUser(id));
        model.addAttribute("user", userService.findOneUser(id).orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "USER HISTORY ERROR")));
        return "game/history_user";
    }

    @GetMapping("/modify_user/{id}")
    public String userModifyGet(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.findOneUser(id).orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "USER MODIFY ERROR")));
        return "game/modify_user";
    }

    @PostMapping("/modify_user")
    public String userModifyPost(@ModelAttribute UserDto userDto) {
        userService.updateUser(userDto);
        return "redirect:/game/table_user";
    }

    @GetMapping("/table_user")
    public String allUsersGet(Model model) {
        model.addAttribute("users", userService.findAllUser());
        return "game/table_user";
    }

    @PostMapping("/table_user")
    public String allUsersPost(Model model, @RequestParam Long id) {
        quizService.addQuiz(QuizDto.builder().user(userService.findOneUser(id).orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "USER FIND ONE ERROR"))).date(LocalDateTime.now()).build());
        model.addAttribute("quiz", quizService.findNewestOneQuiz().orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "QUIZ FIND NEWEST ERROR")));
        model.addAttribute("categoryList", categoryService.findAllCategory());
        model.addAttribute("levelList", Level.values());
        model.addAttribute("questionFilter", new QuestionFilterDto());
        model.addAttribute("user", userService.findOneUser(id).orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "USER FIND ONE ERROR")));
        model.addAttribute("error", "");
        return "game/choose_category";
    }

    //STRONA choose_category

    @PostMapping("/choose_category")
    public String settingGamePost(Model model, @ModelAttribute QuestionFilterDto questionFilter) {
        List<QuestionDto> questionListFilter = questionService.findAllQuestionAfterFiltrs(questionFilter);
        if (questionListFilter.size() >= 5) {
            List<QuestionDto> questionList = questionService.random5question(questionListFilter);
            List<Integer> answers = new ArrayList<>();
            QuizDto quiz = quizService.findNewestOneQuiz().orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "QUIZ FIND NEWEST ERROR"));
            for (QuestionDto q : questionList) {
                gameService.addGame(GameDto.builder().quiz(quiz).question(q).build());
                answers.add(0);
            }
            model.addAttribute("questionsWithAnswers", QuestionsWithAnswersDto.builder().questions(questionList).answers(answers).quiz(quiz).build());
            return "game/quiz";
        } else { //questionListFilter.size() < 5
            QuizDto quiz = quizService.findNewestOneQuiz().orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "QUIZ FIND NEWEST ERROR"));
            model.addAttribute("quiz", quiz);
            model.addAttribute("categoryList", categoryService.findAllCategory());
            model.addAttribute("levelList", Level.values());
            model.addAttribute("questionFilter", new QuestionFilterDto());
            model.addAttribute("user", userService.findOneUser(quiz.getUser().getId()).orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "USER FIND NEWEST ERROR")));
            model.addAttribute("error", "Brak pytań. Proszę wybrać inną kategorię / poziom trudności");
            return "game/choose_category";
        }
    }

    @PostMapping("/delete_quiz")
    public String quizDeletePost(@RequestParam Long id) {
        quizService.deleteQuiz(id);
        return "redirect:/game/table_user";
    }

    //GRA

    @PostMapping("/quiz")
    public String gamePost(Model model, @ModelAttribute QuestionsWithAnswersDto questionsWithAnswersDto) {
        quizService.calculationPoints(questionsWithAnswersDto);
        model.addAttribute("quiz", quizService.findOneQuiz(questionsWithAnswersDto.getQuiz().getId()));
        return "game/end_game";
    }

    @GetMapping("/end_game")
    public String Game() {
        return "game/end_game";
    }
}
