package com.practice.intershop.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String GENERIC_MESSAGE = "Something went wrong 😢";

    @ExceptionHandler(IntershopCustomException.class)
    public String handleException(IntershopCustomException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("error", ex.getMessage() == null ? GENERIC_MESSAGE : ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleAll(Exception ex, Model model) {
        model.addAttribute("error", GENERIC_MESSAGE);
        return "error";
    }
}
