package com.energygames.lojadegames.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    UserDetailsService userDetailsService() {

        return new UserDetailsServiceImpl();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // Substitua pela URL do front-end
        configuration.addAllowedMethod("*"); // Permite todos os métodos (GET, POST, PUT, DELETE, etc.)
        configuration.addAllowedHeader("*"); // Permite todos os cabeçalhos
        configuration.setAllowCredentials(true); // Permite envio de cookies e credenciais

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a configuração globalmente
        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	http
	        .sessionManagement(management -> management
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        		.csrf(csrf -> csrf.disable())
	        		.cors(withDefaults());

    	http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/usuarios/logar").permitAll()
                        .requestMatchers("/usuarios/cadastrar").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/produtos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/categorias").permitAll() // Permite POST em /categorias sem autenticação
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
	        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
	        .httpBasic(withDefaults());

		return http.build();

    }

}