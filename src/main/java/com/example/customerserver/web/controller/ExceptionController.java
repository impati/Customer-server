package com.example.customerserver.web.controller;

import com.example.customerserver.exception.ClientValidException;
import com.example.customerserver.exception.InvalidCodeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ExceptionController {

    @GetMapping("/client")
    public void clientValidError() {
        throw new ClientValidException();
    }

    @GetMapping("/code")
    public void codeValidError() {
        throw new InvalidCodeException();
    }
}
