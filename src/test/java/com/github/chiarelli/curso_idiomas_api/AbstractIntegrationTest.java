package com.github.chiarelli.curso_idiomas_api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MSSQLServerContainer;

@SpringBootTest
public abstract class AbstractIntegrationTest {

    @SuppressWarnings("resource")
    static final MSSQLServerContainer<?> sqlServerContainer = 
        new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest")
            .acceptLicense()
            .withPassword("YourStrong!Passw0rd");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        sqlServerContainer.start();
        registry.add("spring.datasource.url", sqlServerContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlServerContainer::getUsername);
        registry.add("spring.datasource.password", sqlServerContainer::getPassword);
    }
}

