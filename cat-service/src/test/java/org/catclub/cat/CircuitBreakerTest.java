package org.catclub.cat;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.catclub.cat.client.OwnerServiceClient;
import org.catclub.cat.repository.CatRepository;
import org.catclub.shared.dto.OwnerResponse;
import org.catclub.cat.model.Cat;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CircuitBreakerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private CatRepository catRepository;

    @MockBean
    private OwnerServiceClient ownerServiceClient;

    @BeforeEach
    void setup() {
        // Reset circuit breaker
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("ownerServiceClient");
        circuitBreaker.reset();

        // Setup test cat
        Cat testCat = new Cat();
        testCat.setId(1L);
        testCat.setName("Test Cat");
        testCat.setOwnerId(1L);

        when(catRepository.findById(1L)).thenReturn(Optional.of(testCat));
    }


    @Test
    public void whenServiceFails_thenCircuitBreakerOpens() throws Exception {
        // Mock service to throw exception
        when(ownerServiceClient.getOwner(anyLong()))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Make calls - first should fail and trigger fallback
        mockMvc.perform(get("/api/cats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fallback Cat"));

        // Verify circuit breaker metrics
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("ownerServiceClient");
        assertThat(circuitBreaker.getMetrics().getNumberOfFailedCalls()).isEqualTo(1);
    }


    @Test
    public void whenServiceWorks_thenReturnsNormalResponse() throws Exception {
        // Mock successful response
        OwnerResponse successResponse = OwnerResponse.builder()
                .id(1L)
                .name("Test Owner")
                .build();

        when(ownerServiceClient.getOwner(1L))
                .thenReturn(successResponse);

        mockMvc.perform(get("/api/cats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner.name").value("Test Owner"));
    }
}