package com.github.chiarelli.curso_idiomas_api.escola.presentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(@NonNull CorsRegistry registry) {
            registry.addMapping("/**") // Aplica a todos os endpoints
              .allowedOrigins("*") // Libera qualquer origem
              .allowedMethods("*") // Libera todos os métodos HTTP (GET, POST, PUT, DELETE etc.)
              .allowedHeaders("*") // Libera todos os headers
              .allowCredentials(false); // Não permite cookies/autenticação via CORS
        }
      };
    }
}
