FROM openjdk:17-alpine
COPY build/libs/*.jar ./
ENTRYPOINT ["java","-jar","/app.jar"]