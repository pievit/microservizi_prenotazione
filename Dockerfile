# syntax=docker/dockerfile:1.2
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
USER root
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -Dmaven.test.skip clean package

#
# Package stage
#
FROM openjdk:17-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/target/*.jar app.jar
ENV JAVA_ARGS=""
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar