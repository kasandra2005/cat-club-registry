FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/owner-service-*.jar app.jar
COPY src/main/resources/db/changelog /app/db/changelog
EXPOSE 8080 7093
ENTRYPOINT exec java \
            -Dcom.sun.management.jmxremote \
            -Dcom.sun.management.jmxremote.port=7093 \
            -Dcom.sun.management.jmxremote.rmi.port=7093 \
            -Dcom.sun.management.jmxremote.authenticate=false \
            -Dcom.sun.management.jmxremote.ssl=false \
            -Djava.rmi.server.hostname=localhost \
            -XX:+HeapDumpOnOutOfMemoryError \
            -XX:HeapDumpPath=/tmp/heapdump.hprof \
            -jar app.jar