package com.spring.springselenium.Configuraion.config;

import com.github.javafaker.Faker;
import com.spring.springselenium.Configuraion.annotation.LazyConfiguration;
import org.springframework.context.annotation.Bean;

@LazyConfiguration
public class FakerConfig {

    @Bean
    public Faker getFaker(){
        return new Faker();
    }

}
