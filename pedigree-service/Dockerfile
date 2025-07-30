FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/pedigree-service-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]