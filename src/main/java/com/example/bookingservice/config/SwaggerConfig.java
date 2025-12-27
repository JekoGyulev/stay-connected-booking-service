package com.example.bookingservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StayConnected / Booking API")
                        .version("1.0")
                        .description("This API handles reservations")
                        .contact(new Contact()
                                .name("Jeko Gyulev")
                                .email("zhekogyulev@gmail.com"))
                );
    }










}
