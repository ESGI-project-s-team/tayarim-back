FROM maven:amazoncorretto-21-debian as build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine

WORKDIR /app

COPY - from=build /app/target/my-application.jar .

CMD ["java", "-jar", "my-application.jar"]