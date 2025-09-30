package com.example.SIA.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadGatewayException.class)
    public String handleBadGateway(Model model, BadGatewayException ex) {
        model.addAttribute("mensaje", ex.getMessage() != null ? ex.getMessage() : "Error de comunicación con el servidor externo.");
        return "error/502"; // ✅ Aquí está la corrección
    }
}