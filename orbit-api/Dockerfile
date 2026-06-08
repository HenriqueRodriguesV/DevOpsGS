FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S orbitgroup && adduser -S orbituser -G orbitgroup

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar
RUN chown -R orbituser:orbitgroup /app

USER orbituser

ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
