FROM gradle:jdk17-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM eclipse-temurin:17-jdk-jammy

ARG APP_VERSION="0.0.1"

LABEL maintainer = "Aplazo Engineering Team <engineering@aplazo.mx>"
LABEL org.name="Aplazo"
LABEL org.image.title="Aplazo loan REST API service"

RUN mkdir /home/nobody
WORKDIR /home/nobody

COPY --from=build /home/gradle/src/build/libs/loans-service-${APP_VERSION}.jar ./aplazo-loan-service.jar

USER 65534

EXPOSE 8080
ENTRYPOINT ["java","-jar","./aplazo-loan-service.jar"]