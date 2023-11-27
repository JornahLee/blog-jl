# First stage: complete build environment
FROM maven:3.5.0-jdk-8-alpine AS builder

# add pom.xml and source code
COPY mvn-settings.xml /usr/share/maven/conf/settings.xml
ADD ./pom.xml pom.xml
ADD ./src src/
# package jar
RUN mvn clean package

FROM openjdk:8-jdk-alpine
COPY --from=builder target/*.jar app.jar
EXPOSE 8089

RUN echo "java -jar -Xms512M -Xmx512M /app.jar" > /run.sh && chmod 777 /run.sh
ENTRYPOINT ["/bin/sh","/run.sh"]
