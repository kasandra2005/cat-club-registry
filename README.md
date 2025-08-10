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