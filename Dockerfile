FROM openjdk:22-jdk

WORKDIR /app

COPY target/smart-hospital-0.0.1-SNAPSHOT.jar /app/smart-hospital.jar
EXPOSE 8080

CMD ["java", "-jar", "smart-hospital.jar"]