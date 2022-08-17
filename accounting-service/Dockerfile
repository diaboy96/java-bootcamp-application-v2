# Copy built application to docker image and execute it inside of the image
FROM openjdk:18
MAINTAINER martindavidik.cz
COPY target/accounting-service-0.0.1-SNAPSHOT.jar accounting-service.jar
ENTRYPOINT ["java", "-jar", "accounting-service.jar"]