# Copy built application to docker image and execute it inside of the image
FROM openjdk:18
MAINTAINER martindavidik.cz
COPY target/organization-service-0.0.1-SNAPSHOT.jar organization-service.jar
ENTRYPOINT ["java", "-jar", "organization-service.jar"]