package org.catclub.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.gateway.routes[0].id=owner-service",
                "spring.cloud.gateway.routes[0].uri=http://localhost:${wiremock.server.port}",
                "spring.cloud.gateway.routes[0].predicates[0]=Path=/api/owners/**",
                "spring.cloud.gateway.routes[0].filters[0].name=RequestRateLimiter",
                "spring.cloud.gateway.routes[0].filters[0].args.redis-rate-limiter.replenishRate=1",
                "spring.cloud.gateway.routes[0].filters[0].args.redis-rate-limiter.burstCapacity=1",
                "spring.cloud.gateway.routes[0].filters[0].args.redis-rate-limiter.requestedTokens=1",
                "spring.cloud.gateway.routes[0].filters[0].args.key-resolver=#{@remoteAddressKeyResolver}"
        }
)
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
@Testcontainers
@ActiveProfiles("test")
public class RateLimiterTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:alpine"))
            .withExposedPorts(6379);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeAll
    static void beforeAll() {
        redis.start();
        System.out.println("Redis started on port: " + redis.getFirstMappedPort());
    }

    @BeforeEach
    void setup() {
        stubFor(get(urlEqualTo("/api/owners"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @AfterEach
    void verifyRequests() {
        verify(getRequestedFor(urlEqualTo("/api/owners")));
    }

    @Test
    void testRateLimiter_blocksRequestsOverLimit() throws Exception {
        System.out.println("Redis host: " + redis.getHost());
        System.out.println("Redis port: " + redis.getFirstMappedPort());
        System.out.println("WireMock port: " + wireMockServer.port()); // Теперь работает

        // 1. Первый запрос должен пройти
        webTestClient.get().uri("/api/owners")
                .header("X-Forwarded-For", "192.168.1.1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("X-RateLimit-Remaining", "0");

        // 2. Проверяем, что лимит исчерпан
        await().pollInterval(100, MILLISECONDS)
                .atMost(2, SECONDS)
                .untilAsserted(() -> {
                    webTestClient.get().uri("/api/owners")
                            .header("X-Forwarded-For", "192.168.1.1")
                            .exchange()
                            .expectStatus().isEqualTo(429);
                });

        // 3. Ждем восстановления rate limit
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 4. Проверяем, что запросы снова проходят
        webTestClient.get().uri("/api/owners")
                .header("X-Forwarded-For", "192.168.1.1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("X-RateLimit-Remaining", "0");
    }
}