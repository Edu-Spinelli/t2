package br.ufscar.dc.dsw.EstagioT2.config;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Obtém o Locale atual
        Locale locale = LocaleContextHolder.getLocale();

        // Redireciona para o login com o parâmetro de idioma
        response.sendRedirect("/login?logout&lang=" + locale.getLanguage());
    }
}
