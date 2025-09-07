package com.example.frontui.config;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class CsrfModelAdvice {

    // атрибут будет называться "_csrf"
    @ModelAttribute("_csrf")
    public Mono<CsrfToken> csrfToken(ServerWebExchange exchange) {
        return exchange.getAttribute(CsrfToken.class.getName());
    }
}
