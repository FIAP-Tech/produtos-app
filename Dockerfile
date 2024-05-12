FROM azul/zulu-openjdk-alpine:21
EXPOSE 80
COPY target/*.jar produtos-app-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/produtos-app-0.0.1-SNAPSHOT.jar"]
