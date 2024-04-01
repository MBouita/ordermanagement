FROM openjdk:17-alpine

WORKDIR /

COPY build/libs/*.jar ordermanagement-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "ordermanagement-0.0.1-SNAPSHOT.jar"]