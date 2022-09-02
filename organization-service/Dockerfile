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
COPY --from=BUILD /home/app/target/organization-service-0.0.1-SNAPSHOT.jar organization-service.jar
ENTRYPOINT ["java", "-jar", "organization-service.jar"]