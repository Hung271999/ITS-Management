FROM openjdk:17

WORKDIR /app

COPY target/its.management-0.0.1-SNAPSHOT.jar /app/its.management-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=dev

ENV TZ=UTC

CMD ["java", "-jar", "/app/its.management-0.0.1-SNAPSHOT.jar"]
