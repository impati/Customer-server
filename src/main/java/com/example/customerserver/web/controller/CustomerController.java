package com.example.customerserver.web.controller;

import com.example.customerserver.service.customer.CustomerEditor;
import com.example.customerserver.web.data.SimplePrincipal;
import com.example.customerserver.web.request.CustomerEditRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerEditor customerEditor;

    @PostMapping("/v1/customer")
    public SimplePrincipal getSimpleprincipal(SimplePrincipal simplePrincipal) {
        return simplePrincipal;
    }


    @PatchMapping("/v1/customer")
    public void editCustomer(SimplePrincipal simplePrincipal, @RequestBody CustomerEditRequest customerEditRequest) {
        customerEditor.edit(simplePrincipal.id(), customerEditRequest);
    }
}
