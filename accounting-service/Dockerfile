#
# Build stage
#
FROM maven:3.8.6 AS BUILD
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests
# Todo: after integration of TestContainers --> remove -DskipTests

#
# Package stage
#
FROM openjdk:18
MAINTAINER martindavidik.cz
COPY --from=BUILD /home/app/target/accounting-service-0.0.1-SNAPSHOT.jar accounting-service.jar
ENTRYPOINT ["java", "-jar", "accounting-service.jar"]