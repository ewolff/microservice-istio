FROM openjdk:11.0.2-jre-slim
COPY target/microservice-istio-shipping-0.0.1-SNAPSHOT.jar .
CMD /usr/bin/java -Xmx300m -Xms300m -XX:TieredStopAtLevel=1 -noverify -jar microservice-istio-shipping-0.0.1-SNAPSHOT.jar
EXPOSE 8080