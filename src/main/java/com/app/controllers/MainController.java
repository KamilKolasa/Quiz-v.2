package com.app.controllers;

import com.app.service.JSONService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final JSONService jsonService;

    @GetMapping("/")
    public String main(){
        jsonService.addCategoryStandard();
        jsonService.saveQuestions();
        jsonService.getQuestions();
        return "homepage";
    }
}
