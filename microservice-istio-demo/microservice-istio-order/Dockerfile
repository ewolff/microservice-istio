FROM openjdk:11.0.10-jre-slim
COPY target/microservice-istio-order-0.0.1-SNAPSHOT.jar .
CMD java -Xmx300m -Xms300m -XX:TieredStopAtLevel=1 -noverify -jar microservice-istio-order-0.0.1-SNAPSHOT.jar
EXPOSE 8080
