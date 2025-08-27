package com.documents.tarefas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gerenciamento de Tarefas")
                        .description("API REST para gerenciamento completo de tarefas com funcionalidades de CRUD, " +
                                   "filtros, busca, status e prioridades. Desenvolvida seguindo as melhores práticas " +
                                   "de programação orientada a objetos e padrão MVC.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Meu portifólio")
                                .email("marcossanches710@gmail.com")
                                .url("https://github.com/Marcos1020")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:9293")
                                .description("Servidor Local")
        ));
    }
}
