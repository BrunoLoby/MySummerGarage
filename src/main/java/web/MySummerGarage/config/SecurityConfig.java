package web.MySummerGarage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos e login liberados
                .requestMatchers("/css/**", "/js/**", "/images/**",
                                 "/webjars/**", "/favicon.ico").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                // Home pública (vitrine de carros) e detalhes do anúncio acessíveis sem login
                .requestMatchers("/").permitAll()
                .requestMatchers("/anuncio/visualizar/**").permitAll()
                // Apenas ADMIN acessa usuários e relatórios
                .requestMatchers("/usuario/**").hasRole("ADMIN")
                .requestMatchers("/relatorios/**").hasRole("ADMIN")
                // Qualquer outra requisição exige autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/403")
            );

        return http.build();
    }
}