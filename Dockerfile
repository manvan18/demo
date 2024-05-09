FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar ./demo-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
