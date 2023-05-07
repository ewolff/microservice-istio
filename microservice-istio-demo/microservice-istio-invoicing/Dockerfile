FROM eclipse-temurin:20.0.1_9-jre-alpine
COPY target/microservice-istio-invoicing-0.0.1-SNAPSHOT.jar .
CMD java -Xmx300m -Xms300m -XX:TieredStopAtLevel=1 -noverify -jar microservice-istio-invoicing-0.0.1-SNAPSHOT.jar
EXPOSE 8080
