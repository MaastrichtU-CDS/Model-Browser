FROM openjdk:11

# install webapplication
COPY target/model-commissioning-station-0.0.1-SNAPSHOT.jar model-commissioning-station-0.0.1-SNAPSHOT.jar

EXPOSE 8080
CMD ["java", "-jar", "model-commissioning-station-0.0.1-SNAPSHOT.jar"]