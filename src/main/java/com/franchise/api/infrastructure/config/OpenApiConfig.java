package com.franchise.api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API.
 * Proporciona información detallada sobre la API y sus endpoints.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers());
    }

    /**
     * Información general de la API.
     */
    private Info apiInfo() {
        return new Info()
                .title("Franchise Management API")
                .description("""
                        API REST para la gestión de franquicias, sucursales y productos.
                        
                        ## Características
                        - Gestión completa de franquicias y sus sucursales
                        - Control de inventario de productos por sucursal  
                        - Consultas optimizadas para productos con mayor stock
                        - Arquitectura hexagonal con programación reactiva
                        - Validaciones de negocio robustas
                        
                        ## Arquitectura
                        Esta API está desarrollada con Spring Boot WebFlux y sigue los principios de:
                        - **Clean Architecture (Hexagonal)**
                        - **Programación Reactiva** con Mono y Flux
                        - **Domain Driven Design (DDD)**
                        - **CQRS pattern** para separar comandos de consultas
                        
                        ## Base de Datos
                        Utiliza MongoDB como base de datos NoSQL con Spring Data MongoDB Reactive.
                        """)
                .version("v1.0.0")
                .contact(apiContact())
                .license(apiLicense());
    }

    /**
     * Información de contacto del desarrollador.
     */
    private Contact apiContact() {
        return new Contact()
                .name("Franchise API Team")
                .email("franchise-api@example.com")
                .url("https://github.com/username/franchise-api");
    }

    /**
     * Información de licencia.
     */
    private License apiLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    /**
     * Servidores donde está disponible la API.
     */
    private List<Server> apiServers() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Servidor de desarrollo local");
        
        Server dockerServer = new Server()
                .url("http://localhost:8081")
                .description("Servidor Docker");

        return List.of(localServer, dockerServer);
    }
}
