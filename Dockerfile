#FROM openjdk:17-jdk-alpine
#COPY /target/VenteLivreEnLigne-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]
FROM maven

WORKDIR /src

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

CMD ["java", "-jar", "target/VenteLivreEnLigne-0.0.1-SNAPSHOT.jar"]