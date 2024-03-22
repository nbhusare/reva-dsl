FROM openjdk:17-jdk
COPY reva.ls/build/libs/reva.ls-1.0.0-SNAPSHOT-ls.jar lang-server.jar
ENTRYPOINT ["java", "-jar", "lang-server.jar"]
EXPOSE 5008