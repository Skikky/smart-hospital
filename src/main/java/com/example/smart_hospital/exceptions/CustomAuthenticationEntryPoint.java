package com.example.smart_hospital.exceptions;

import com.example.smart_hospital.responses.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse("LoginFailedException", "devi fare il login per accedere");
        String myMessage = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(myMessage);
    }
}
