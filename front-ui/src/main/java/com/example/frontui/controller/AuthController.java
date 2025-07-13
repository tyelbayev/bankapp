package com.example.frontui.controller;
import com.example.frontui.client.AccountsClient;
import com.example.frontui.dto.SignupRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {

    private final AccountsClient accountsClient;

    public AuthController(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupRequest request,
                         BindingResult result,
                         Model model) {
        List<String> errors = accountsClient.validateAndRegister(request);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("signupRequest", request);
            return "signup";
        }
        return "redirect:/main";
    }
}
