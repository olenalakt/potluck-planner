FROM openjdk:11
ADD target/oauth-server-0.0.1-SNAPSHOT.jar /oauth-server-0.0.1-SNAPSHOT.jar
ADD jwt.jks /opt/jwt.jks
ENTRYPOINT ["java", "-jar", "oauth-server-0.0.1-SNAPSHOT.jar"]