FROM eclipse-temurin:24.0.1_9-jre-alpine
COPY target/microservice-istio-bonus-0.0.1-SNAPSHOT.jar .
CMD java -Xmx300m -Xms300m -XX:TieredStopAtLevel=1 -noverify -jar microservice-istio-bonus-0.0.1-SNAPSHOT.jar
EXPOSE 8080