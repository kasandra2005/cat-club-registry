package org.catclub.owner;

import org.catclub.owner.dto.OwnerRequest;
import org.catclub.owner.service.OwnerService;
import org.catclub.shared.dto.OwnerResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class OwnerServiceTest {
    @Autowired
    private OwnerService ownerService;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("catclub")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        // Ждем пока БД будет готова
        postgres.start();
    }

    @Test
    void shouldCreateAndRetrieveOwner() {
        // Подготовка тестовых данных
        OwnerRequest request = new OwnerRequest(
                "Test Owner",
                "test@example.com",
                "+1234567890"
        );

        // Вызов тестируемого метода
        OwnerResponse createdResponse = ownerService.createOwner(request);

        // Проверка создания
        assertThat(createdResponse.getId()).isNotNull();
        assertThat(createdResponse.getName()).isEqualTo("Test Owner");
        assertThat(createdResponse.getEmail()).isEqualTo("test@example.com");
        assertThat(createdResponse.getPhone()).isEqualTo("+1234567890");
        assertThat(createdResponse.getRegistrationDate()).isNotNull();

        // Получение созданного владельца
        OwnerResponse retrievedResponse = ownerService.getOwner(createdResponse.getId());

        // Проверки
        assertThat(retrievedResponse.getId()).isEqualTo(createdResponse.getId());
        assertThat(retrievedResponse.getName()).isEqualTo("Test Owner");
        assertThat(retrievedResponse.getEmail()).isEqualTo("test@example.com");
        assertThat(retrievedResponse.getPhone()).isEqualTo("+1234567890");
        assertThat(retrievedResponse.getRegistrationDate())
                .isEqualTo(createdResponse.getRegistrationDate());
    }
}