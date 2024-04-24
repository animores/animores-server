FROM eclipse-temurin:17-jdk-alpine
COPY animores-server-api-1.0.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]