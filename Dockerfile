FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-ubi-minimal

WORKDIR /work/

COPY target/lib /work/application/lib
COPY target/*.jar /work/application/
RUN chmod 775 /work

EXPOSE 8080

CMD exec java -Dquarkus.http.host=0.0.0.0 -jar /work/application/*-runner.jar
