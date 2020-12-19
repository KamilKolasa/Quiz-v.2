package com.app.controllers;

import com.app.exceptions.MyException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({MyException.class})
    public String myExceptionHandler(MyException e, Model model) {
        System.out.println("--------------------------------------------------");
        System.out.println(e.getMessage());
        e.printStackTrace();
        System.out.println("--------------------------------------------------");
        model.addAttribute("code", e.getExceptionInfo().getCode());
        model.addAttribute("message", e.getExceptionInfo().getMessage());
        model.addAttribute("date", e.getExceptionInfo().getExceptionDateTime());
        return "errorPage";
    }
}
