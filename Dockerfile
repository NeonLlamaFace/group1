FROM amazoncorretto:17
COPY ./target/semApp.jar /tmp
COPY statements.sql /tmp/statements.sql
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "semApp.jar", "statements.sql"]