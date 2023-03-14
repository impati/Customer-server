package com.example.customerserver.web.config;

import com.example.customerserver.web.argument.CodeArgumentResolver;
import com.example.customerserver.web.token.CodeGenerator;
import com.example.customerserver.web.token.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenGenerator codeGenerator;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CodeArgumentResolver(codeGenerator));
    }
}
