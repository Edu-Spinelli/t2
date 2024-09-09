package br.ufscar.dc.dsw.EstagioT2.config;

import br.ufscar.dc.dsw.EstagioT2.config.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/vagas", "/register/**", "/register/empresa**", "/register/profissional**", "/login/**").permitAll() // Permite o acesso às rotas de registro
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/empresa/**").hasRole("EMPRESA")
                        .requestMatchers("/profissional/**").hasRole("PROFISSIONAL")
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/public/**").permitAll() // Permitir acesso a recursos estáticos

                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")  // Redireciona para a página de login após o logout
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

