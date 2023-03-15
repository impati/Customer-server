package com.example.customerserver.web.controller;

import com.example.customerserver.web.data.SimplePrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @PostMapping("/v1/customer")
    public SimplePrincipal getSimpleprincipal(SimplePrincipal simplePrincipal) {
        return simplePrincipal;
    }

}
