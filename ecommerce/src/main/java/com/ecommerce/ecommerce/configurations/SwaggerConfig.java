package com.ecommerce.ecommerce.configurations;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-Commerce REST API",
                version = "1.0",
                description = "API for managing products, orders, users, payments, " +
                        "with security and more.",
                contact = @Contact(name = "Guillermo Rivadeneira",
                        email = "guillerivadeneira7@gmail.com")
        )
)
public class SwaggerConfig {
}
