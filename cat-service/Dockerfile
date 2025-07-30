FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/cat-service-*.jar app.jar
COPY target/classes/db /app/classes/db
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]