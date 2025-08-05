FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/owner-service-*.jar app.jar
COPY src/main/resources/db/changelog /app/db/changelog
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]