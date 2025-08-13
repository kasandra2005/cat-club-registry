Cat Club Registry - Система регистрации породистых кошек

Описание проекта:
Cat Club Registry - это микросервисное приложение для учета владельцев породистых кошек и их питомцев. Система позволяет:
- Регистрировать владельцев кошек с контактными данными
- Добавлять информацию о кошках (кличка, порода, окраси т.д.)
- Управлять присвоением родословных кошкам

Архитектура:
- API Gateway - единая точка входа для всех запросов
- Owner Service - управление владельцами кошек
- Cat Service - управление информацией о кошках
- Pedigree Service - управление родословными
- Shared-lib - библиотека для общего использования

Технологический стек:
- Java 21
- Spring Boot
- Spring Cloud
- PostgreSQL
- Redis
- Liquibase
- Docker
- Kubernetes (Helm) (с развертыванием возникли проблемы, поэтому в последнем блоке о возникших проблемах указано,
что именно было сделано, но в проект не закоммитила нерабочий вариант)
- Prometheus + Grafana (в Grafana с отображением данных на дашбордах так же возникли проблемы, 
- это также указано в последнем блоке о возникших проблемах данного файла)
- OpenAPI 3.0
___________________________________________________________________________
Запуск:
# В корне проекта (с pom.xml)
mvn clean package

Создайте Docker-образы:
docker-compose build

Запустите все сервисы:
docker-compose up -d

Проверим статус всех контейнеров:
docker-compose ps

➜  cat-club-registry git:(main) ✗ docker-compose ps

NAME               IMAGE                    COMMAND                  SERVICE            CREATED         STATUS                     PORTS
api-gateway        api-gateway              "java -Dspring.profi…"   api-gateway        8 minutes ago   Up 8 minutes (healthy)     0.0.0.0:8080->8080/tcp, [::]:8080->8080/tcp
cat-service        cat-service              "java -jar app.jar"      cat-service        8 minutes ago   Up 8 minutes (healthy)     0.0.0.0:8082->8080/tcp, [::]:8082->8080/tcp
grafana            grafana/grafana:latest   "/run.sh"                grafana            8 minutes ago   Up 4 seconds               0.0.0.0:3000->3000/tcp, [::]:3000->3000/tcp
owner-service      owner-service            "java -jar app.jar"      owner-service      8 minutes ago   Up 8 minutes (healthy)     0.0.0.0:8081->8080/tcp, [::]:8081->8080/tcp
pedigree-service   pedigree-service         "java -jar app.jar"      pedigree-service   8 minutes ago   Up 8 minutes (healthy)     0.0.0.0:8083->8080/tcp, [::]:8083->8080/tcp
postgres           postgres:15              "docker-entrypoint.s…"   postgres           8 minutes ago   Up 8 minutes (healthy)     0.0.0.0:5432->5432/tcp, [::]:5432->5432/tcp
prometheus         prom/prometheus:latest   "/bin/prometheus --c…"   prometheus         8 minutes ago   Up 8 minutes (unhealthy)   0.0.0.0:9090->9090/tcp, [::]:9090->9090/tcp
_____________________________________________________________________________________________
Проверка эндпоинтов:

API Gateway:
![api-gateway-up.png](img/api-gateway-up.png)
Owner Service:
![owner-service-up.png](img/owner-service-up.png)
Cat Service:
![cat-service-up.png](img/cat-service-up.png)
Pedigree Service:
![pedigree-service-up.png](img/pedigree-service-up.png)

Создание владельца:
curl --location 'http://localhost:8080/api/owners' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Иван Петров",
"email": "ivan@example.com",
"phone": "+79161234567"
}'
![owner-create.png](img/owner-create.png)

Просмотри владельца:
curl --location 'http://localhost:8080/api/owners/1'
![owner-get.png](img/owner-get.png)

Создание кота:
curl --location 'http://localhost:8080/api/cats' \
--header 'Content-Type: application/json' \
--data '{
"name": "Барсик",
"breed": "Британский",
"color": "Серый",
"ownerId": 1,
"birthDate": "2020-05-15",
"gender": "MALE"
}'
![cat-create.png](img/cat-create.png)

Просмотр кота:
curl --location 'http://localhost:8080/api/cats/1'
![cat-get.png](img/cat-get.png)

Задание родословной:
curl --location 'http://localhost:8080/api/pedigrees' \
--header 'Content-Type: application/json' \
--data '{
"catId": 1,
"registrationNumber": "PED-001"
}'
![pedigree-create.png](img/pedigree-create.png)

Просмотр родословной:
curl --location 'http://localhost:8080/api/pedigrees/1'
![pedigree-get.png](img/pedigree-get.png)

_________________________________________________________

Документация:

Swagger UI и api-docs:
Owner Service:
http://localhost:8081/swagger-ui/index.html
http://localhost:8081/v3/api-docs/owner-service
![owner-swagger.png](img/owner-swagger.png)
![owner-docs.png](img/owner-docs.png)

Cat Service:
http://localhost:8082/swagger-ui/index.html
http://localhost:8082/v3/api-docs/cat-service
![cat-swagger.png](img/cat-swagger.png)
![docs-cat.png](img/docs-cat.png)

Pedigree Service:
http://localhost:8083/swagger-ui/index.html
http://localhost:8083/v3/api-docs/pedigree-service
![pedigree-swagger.png](img/pedigree-swagger.png)
![docs-pedigree.png](img/docs-pedigree.png)
__________________________________________________________

Мониторинг и метрики:

Система мониторинга включает:
Prometheus (http://localhost:9090) - сбор метрик
Grafana (http://localhost:3000) - визуализация (логин: admin/admin)

Prometheus targets доступны здесь: http://localhost:9090/targets
![prometheus.png](img/prometheus.png)

Основные метрики:
Общее количество запросов:
http_server_requests_seconds_count{uri!~"/actuator.*"}
![total-requests-table.png](img/total-requests-table.png)
![total-requests-graph.png](img/total-requests-graph.png)

Кастомные метрики:
Создание кота:
cat_create_count_total
![cat_create_table.png](img/cat_create_table.png)
![cat_create_graph.png](img/cat_create_graph.png)
Создание владельца:
owner_create_count_total

Просмотр всех метрик в Prometheus:
http://localhost:8080/actuator/prometheus
_______________________________________________________________

JMeter:
Тестировались ендпоинты:
- Создание владельцев (/api/owners)
- Создание кошек (/api/cats)
- Получение информации о владельце (/api/owners/{id})

Тестовые данные находятся в файле:
[test-data.csv](jmeter/test-data.csv)
Тест план:
[cat-club-test.jmx](jmeter/cat-club-test.jmx)
Результаты:
![view-result-tree.png](img/view-result-tree.png)
![summary-report.png](img/summary-report.png)
_________________________________________________________________

Анализ в MemoryAnalyzer (MAT):
Heap dump создан под нагрузкой командой:
docker exec owner-service jcmd 1 GC.heap_dump /tmp/heap_under_load.hprof

В результате:
Leak Suspects:
![leak-suspects.png](img/leak-suspects.png)
![leak-suspects-2.png](img/leak-suspects-2.png)

Dominator Tree:
![dominator-tree.png](img/dominator-tree.png)

Histogram:
![histogram.png](img/histogram.png)

Анализ памяти:
- Общий размер кучи: 74.3 MB
- Ключевые объекты:
    - Spring прокси: 37KB (малозначимо)
    - Кэши: ~12% памяти
- Неограниченный кэш Caffeine (риск утечки)
- 14 прокси-методов OwnerController
__________________________________________________________________
VisualVM:
Добавлен JMX для мониторинга через VisualVM для owner-service
Через GUI VisualVM добавила JMX Connection:
service:jmx:rmi:///jndi/rmi://localhost:7093/jmxrmi
Запустила минимальную нагрузку через jmeter (тест план прикреплен к проекту)
Скриншоты:
Overview:
![visualvm-overwiew.png](img/visualvm-overwiew.png)
Monitor:
![visualvm-monitor.png](img/visualvm-monitor.png)
Threads:
![visualvm-threds.png](img/visualvm-threds.png)
Sampler:
![visualvl-sampler.png](img/visualvl-sampler.png)
__________________________________________________________________
Rate Limiter и Circuit Breaker
Реализация Rate Limiter в API Gateway:

Ограничение количества запросов:
- Базовый лимит: 10 запросов в секунду
- Burst-емкость: 20 запросов (максимальный всплеск)

Техническая реализация:
- Добавлен сервис Redis в docker-compose.yml
- Настроено подключение в application.yml API Gateway

Реализован RequestRateLimiter
KeyResolver определяет клиента по IP-адресу (с fallback на "test-key" для тестов)

Поведение при превышении лимита:
- Возвращается HTTP 429 Too Many Requests

Тестирование:
- Написаны интеграционные тесты с использованием Testcontainers и WireMock 
[RateLimiterTest.java](api-gateway/src/test/java/org/catclub/gateway/RateLimiterTest.java)
- Для тестов используется конфигурация в application-test.yml
[application-test.yaml](api-gateway/src/test/resources/application-test.yaml)

Реализация Circuit Breaker в Cat Service:
Защита от сбоев при вызовах OwnerService
Реализация через:
- Feign Client с Circuit Breaker
- Двухуровневый fallback механизм (Feign + Service level)

Тестирование:
- Написаны интеграционные тесты с использованием Testcontainers и WireMock
[CircuitBreakerTest.java](cat-service/src/test/java/org/catclub/cat/CircuitBreakerTest.java)
- Для тестов используется конфигурация в application-test.yml
[application-test.yml](cat-service/src/test/resources/application-test.yml)
__________________________________________________________________
Возникшие проблемы:
- Попытка отображения метрик в grafana:
API Latency (95th & 99th percentiles), API Request Rate, API Error Rate (%), System Resources,
Feign Client Calls и Business Metrics
Сам dashboard в grafana виден:
![dashboard.png](img/dashboard.png)
Он лежит в приложении:
[cat-club-main.json](monitoring/grafana/dashboards/cat-club-main.json)
Но детальная информация видна только по System Resources и Business Metrics
По остальным "No data":
![dashboard-details.png](img/dashboard-details.png)

- Попытка запуска приложения в kubernetes с использованием helm, kind, kubectl
Была создана директория helm c необходимыми директориями и файлами:
![helm.png](img/helm.png)
Также были созданы файлы kind-config и values в основной директории проекта:
![king-config.png](img/king-config.png)
![values.png](img/values.png)
Возникли проблемы с развертыванием из-за зависимости сервисов, часть сервисов не работали корректно, 
после решения проблем с ними возникали проблемы с другими. 