package com.example.smart_hospital.exceptions;

import com.example.smart_hospital.responses.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedEntryPoint implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse("UnauthorizedException", "non hai l'autorizzazione per accedere a questo endpoint");
        String myMessage = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(myMessage);
    }
}
