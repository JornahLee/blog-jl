# First stage: complete build environment
FROM maven:3.5.0-jdk-8-alpine AS builder

# add pom.xml and source code
ADD ./pom.xml pom.xml
ADD ./src src/
# package jar
RUN mvn clean package

FROM openjdk:8-jdk-alpine
COPY ${JAR_FILE} app.jar
COPY --from=builder target/*.jar app.jar

RUN echo "java -jar /app.jar" > /run.sh && chmod 777 /run.sh
ENTRYPOINT ["/bin/sh","/run.sh"]
