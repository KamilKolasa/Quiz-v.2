package com.app.controllers;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Level;
import com.app.model.dto.AnswerDto;
import com.app.model.dto.CategoryDto;
import com.app.model.dto.QuestionDto;
import com.app.model.dto.QuestionFilterDto;
import com.app.service.AnswerService;
import com.app.service.CategoryService;
import com.app.service.JSONService;
import com.app.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/configuration")
public class ConfigurationController {

    private final CategoryService categoryService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final JSONService jsonService;

    @GetMapping("/all_questions")
    public String questionsAllGet(Model model) {
        model.addAttribute("categoryList", categoryService.findAllCategory());
        model.addAttribute("questions", questionService.findAllQuestion());
        model.addAttribute("levelList", Level.values());
        model.addAttribute("questionFilter", new QuestionFilterDto());
        return "configuration/all_questions";
    }

    @PostMapping("/all_questions")
    public String questionsAllPost(Model model, @ModelAttribute QuestionFilterDto questionFilter) {
        model.addAttribute("categoryList", categoryService.findAllCategory());
        model.addAttribute("levelList", Level.values());
        model.addAttribute("questions", questionService.findAllQuestionAfterFiltrs(questionFilter));
        model.addAttribute("questionFilter", new QuestionFilterDto());
        return "configuration/all_questions";
    }

    @GetMapping("/insert_question")
    public String questionInsertGet(Model model) {

        QuestionDto questionDto = new QuestionDto();
        questionDto.setAnswers(Arrays.asList(new AnswerDto(), new AnswerDto(), new AnswerDto(), new AnswerDto()));

        model.addAttribute("category", categoryService.findAllCategory().stream().filter(x -> !x.getName().equals("WSZYSTKO")).collect(Collectors.toList()));
        model.addAttribute("levelList", Arrays.stream(Level.values()).filter(x -> !x.equals(Level.ALL)).collect(Collectors.toList()));
        model.addAttribute("goodAnswerList", Arrays.asList(1, 2, 3, 4));
        model.addAttribute("question", questionDto);

        return "configuration/insert_question";
    }

    @PostMapping("/insert_question")
    public String questionInsertPost(@ModelAttribute QuestionDto questionDto) {
        QuestionDto question = questionService.addQuestion(questionDto);
        answerService.addListAnswers(questionDto, question.getId());
        return "redirect:/configuration/all_questions";
    }

    @GetMapping("/details_question/{id}")
    public String questionDetailsGet(Model model, @PathVariable Long id) {
        QuestionDto questionDto = questionService.findOneQuestion(id).orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "QUESTION DETAILS ERROR"));
        model.addAttribute("question", questionDto);
        model.addAttribute("answer", questionDto.getAnswers().get(questionDto.getGoodAnswer() - 1));
        return "configuration/details_question";
    }

    @PostMapping("/delete_question")
    public String questionDeletePost(@RequestParam Long id) {
        questionService.deleteQuestion(id);
        return "redirect:/configuration/all_questions";
    }

    @GetMapping("/modify_question/{id}")
    public String questionModifyGet(Model model, @PathVariable Long id) {
        model.addAttribute("category", categoryService.findAllCategory().stream().filter(x -> !x.getName().equals("WSZYSTKO")).collect(Collectors.toList()));
        model.addAttribute("levelList", Arrays.stream(Level.values()).filter(x -> !x.equals(Level.ALL)).collect(Collectors.toList()));
        model.addAttribute("goodAnswerList", Arrays.asList(1, 2, 3, 4));
        model.addAttribute("question", questionService.findOneQuestion(id).orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "QUESTION MODIFY ERROR")));
        return "configuration/modify_question";
    }

    @PostMapping("/modify_question")
    public String questionModifyPost(@ModelAttribute QuestionDto questionDto) {
        questionService.updateQuestion(questionDto);
        return "redirect:/configuration/all_questions";
    }

    @GetMapping("/json")
    public String getDataFromJsonFile() {
        jsonService.getQuestions();
        return "redirect:/configuration/all_questions";
    }

    //KATEGORIE

    @GetMapping("/edit_category")
    public String allCategorysGet(Model model) {
        model.addAttribute("category", new CategoryDto());
        model.addAttribute("map", categoryService.countCategories());
        return "configuration/edit_category";
    }

    @PostMapping("/edit_category")
    public String CategoryInsertPost(@ModelAttribute CategoryDto categoryDto) {
        categoryService.addCategory(categoryDto);
        return "redirect:/configuration/edit_category";
    }

    @PostMapping("/delete_category")
    public String categoryDeleteGet(@RequestParam Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/configuration/edit_category";
    }

    @GetMapping("/modify_category/{id}")
    public String categoryModifyGet(Model model, @PathVariable Long id) {
        model.addAttribute("category", categoryService.findOneCategory(id).orElseThrow(() -> new MyException(ExceptionCode.CONTROLLER, "CATEGORY MODIFY ERROR")));
        return "configuration/modify_category";
    }

    @PostMapping("/modify_category")
    public String categoryModifyPost(@ModelAttribute CategoryDto categoryDto) {
        categoryService.updateCategory(categoryDto);
        return "redirect:/configuration/edit_category";
    }
}
