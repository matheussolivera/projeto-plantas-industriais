package br.edu.eseg.plantas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estaticos e login publicos
                .requestMatchers("/login", "/css/**", "/js/**", "/error", "/acesso-negado").permitAll()
                // Console H2 apenas para desenvolvimento local
                .requestMatchers("/h2-console/**").hasRole("ADMIN")
                // Operacoes administrativas protegidas no servidor (ROLE_ADMIN)
                .requestMatchers("/plantas/nova").hasRole("ADMIN")
                .requestMatchers("/plantas/*/editar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/plantas/*/excluir").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/plantas/*/status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/plantas/*/linhas/*/excluir").hasRole("ADMIN")
                .requestMatchers("/auditoria/**").hasRole("ADMIN")
                // Demais operacoes de consulta e gestao operacional de linhas (ADMIN e OPERADOR)
                .requestMatchers("/", "/dashboard", "/plantas/**").hasAnyRole("ADMIN", "OPERADOR")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/acesso-negado")
            )
            // Mantem CSRF ativo na aplicacao e libera apenas frames/CSRF do console H2
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
