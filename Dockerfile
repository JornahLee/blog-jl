FROM java:8-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN echo "java -jar /app.jar" > /run.sh && chmod 777 /run.sh
ENTRYPOINT ["/bin/sh","/run.sh"]
