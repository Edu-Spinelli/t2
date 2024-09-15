package br.ufscar.dc.dsw.EstagioT2.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        System.out.println("User role: " + role);

        if (role.equals("ROLE_EMPRESA")) {
            System.out.println("Redirecting to /empresa/home");
            response.sendRedirect("/empresa/home");
        } else if (role.equals("ROLE_PROFISSIONAL")) {
            System.out.println("Redirecting to /profissional/home");
            response.sendRedirect("/profissional/home");
        } else if (role.equals("ROLE_ADMIN")) {
            System.out.println("Redirecting to /admin/home");
            response.sendRedirect("/admin/home");
        } else {
            System.out.println("Redirecting to /home");
            response.sendRedirect("/home");
        }
    }
}
