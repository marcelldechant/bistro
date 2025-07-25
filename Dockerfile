FROM openjdk:21
LABEL authors="Marcell Dechant"
EXPOSE 8080
COPY target/bistro.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]