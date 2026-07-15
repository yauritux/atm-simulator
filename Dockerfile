FROM maven:3.9-eclipse-temurin-21 AS build
LABEL maintainer="yauritux@gmail.com"

COPY . /usr/local/atm-simulator

WORKDIR /usr/local/atm-simulator/

RUN mvn -Dmaven.test.skip=true clean package

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /usr/local/atm-simulator/core/target/core-1.0.jar /app/
COPY --from=build /usr/local/atm-simulator/cli-application/target/cli-application-1.0.jar /app/

CMD ["java", "-cp", "core-1.0.jar:cli-application-1.0.jar", "com.dkatalis.AppRunner"]