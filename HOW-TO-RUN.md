# How to Run

This is a step-by-step guide how to run the example:

## Installation

* Install
[minikube](https://github.com/kubernetes/minikube/releases). Minikube
is a Kubernetes environment in a virtual machine that is easy to use
and install. It is not meant for production but to test Kubernetes or
for developer environments.

* Create a Minikube instance with `minikube start --cpus=2 --memory=6000`. This
  will set the memory of the Kubernetes VM to 6.000 MB - which should
  be enough for most experiments. You might want to adjust the number of CPUs
  depending on you local machine.

```
[~/microservice-istio]minikube start --memory=6000
Starting local Kubernetes v1.12.4 cluster...
Starting VM...
Getting VM IP address...
Moving files into cluster...
Setting up certs...
Connecting to cluster...
Setting up kubeconfig...
Starting cluster components...
Kubectl is now configured to use the cluster.
```

* Install
  [kubectl](https://kubernetes.io/docs/tasks/kubectl/install/). This
  is the command line interface for Kubernetes.

* Install [istio](https://istio.io/docs/setup/kubernetes/quick-start/)

## Build the Docker images

* The example is implemented in Java. See
   https://www.java.com/en/download/help/download_options.xml about how
   to download Java. The
   examples need to be compiled so you need to install a JDK (Java
   Development Kit). A JRE (Java Runtime Environment) is not
   sufficient. After the installation you should be able to execute
   `java` and `javac` on the command line.

* The example run in Docker Containers. You need to install Docker
  Community Edition, see https://www.docker.com/community-edition/
  . You should be able to run `docker` after the installation.

Change to the directory `microservice-isitio-demo` and run `./mvnw clean
package` or `mvnw.cmd clean package` (Windows). This will take a while:

```
[~/microservice-istio/microservice-istio]./mvnw clean package
....
[INFO] 
[INFO] --- maven-jar-plugin:2.6:jar (default-jar) @ microservice-istio-order ---
[INFO] Building jar: /Users/wolff/microservice-istio/microservice-istio/microservice-istio-order/target/microservice-istio-order-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:1.4.5.RELEASE:repackage (default) @ microservice-istio-order ---
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] microservice-istio ....................... SUCCESS [  0.986 s]
[INFO] microservice-istio-invoicing .............. SUCCESS [ 16.953 s]
[INFO] microservice-istio-shipping ............... SUCCESS [ 18.016 s]
[INFO] microservice-istio-order ................. SUCCESS [ 18.512 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 57.633 s
[INFO] Finished at: 2017-09-08T09:36:32+02:00
[INFO] Final Memory: 56M/420M
[INFO] ------------------------------------------------------------------------
```

If this does not work:

* Ensure that `settings.xml` in the directory `.m2` in your home
directory contains no configuration for a specific Maven repo. If in
doubt: delete the file.

* The tests use some ports on the local machine. Make sure that no
server runs in the background.

* Skip the tests: `./mvnw clean package -Dmaven.test.skip=true` or
  `mvnw.cmd clean package -Dmaven.test.skip=true` (Windows).

* In rare cases dependencies might not be downloaded correctly. In
  that case: Remove the directory `repository` in the directory `.m2`
  in your home directory. Note that this means all dependencies will
  be downloaded again.

Now the Java code has been compiles. The next step is to create Docker
images:

* Configure Docker so that it uses the Kubernetes cluster to install the
Docker images: `minikube.exe docker-env` tells you how to do that.

* Afterwards you should see the Docker images of Kubernetes if you do `docker images`:

```
[~/microservice-istio/microservice-istio]docker images
REPOSITORY                                TAG                 IMAGE ID            CREATED             SIZE
k8s.gcr.io/kubernetes-dashboard-amd64     v1.10.1             f9aed6605b81        3 weeks ago         122MB
k8s.gcr.io/kube-proxy                     v1.12.4             6d393e89739f        3 weeks ago         96.5MB
k8s.gcr.io/kube-apiserver                 v1.12.4             c04b373449d3        3 weeks ago         194MB
k8s.gcr.io/kube-controller-manager        v1.12.4             51b2a8e5ff78        3 weeks ago         164MB
k8s.gcr.io/kube-scheduler                 v1.12.4             c1b5e63c0b56        3 weeks ago         58.4MB
istio/sidecar_injector                    1.0.5               091fd902183a        4 weeks ago         52.9MB
istio/servicegraph                        1.0.5               cef5bb589599        4 weeks ago         16.5MB
istio/proxyv2                             1.0.5               e393f805ceac        4 weeks ago         380MB
istio/pilot                               1.0.5               68f5cc3a87ff        4 weeks ago         313MB
istio/mixer                               1.0.5               582d5c76010e        4 weeks ago         70MB
istio/galley                              1.0.5               e35efbcb45ed        4 weeks ago         73.1MB
istio/citadel                             1.0.5               3e6285f52cd0        4 weeks ago         56.1MB
k8s.gcr.io/etcd                           3.2.24              3cab8e1b9802        3 months ago        220MB
k8s.gcr.io/coredns                        1.2.2               367cdc8433a4        4 months ago        39.2MB
grafana/grafana                           5.2.3               17a5ba3b1216        4 months ago        245MB
prom/prometheus                           v2.3.1              b82ef1f3aa07        6 months ago        119MB
jaegertracing/all-in-one                  1.5                 93f16463fee4        7 months ago        48.4MB
k8s.gcr.io/kube-addon-manager             v8.6                9c16409588eb        10 months ago       78.4MB
k8s.gcr.io/pause                          3.1                 da86e6ba6ca1        12 months ago       742kB
gcr.io/k8s-minikube/storage-provisioner   v1.8.1              4689081edb10        14 months ago       80.8MB
quay.io/coreos/hyperkube                  v1.7.6_coreos.0     2faf6f7a322f        15 months ago       699MB
```

* Run `docker-build.sh` in the directory
`microservice-istio`. It builds the images and uploads them into the
Kubernetes cluster.

```
[~/microservice-istio/microservice-istio]./docker-build.sh 
...
Successfully tagged microservice-istio-invoicing:latest
Sending build context to Docker daemon  47.88MB
Step 1/4 : FROM openjdk:10.0.2-jre-slim
 ---> 4bd06752ac4a
Step 2/4 : COPY target/microservice-istio-order-0.0.1-SNAPSHOT.jar .
 ---> 31d666e6ecab
Step 3/4 : CMD /usr/bin/java -Xmx400m -Xms400m -jar microservice-istio-order-0.0.1-SNAPSHOT.jar
 ---> Running in 6a3aafef3449
Removing intermediate container 6a3aafef3449
 ---> a83eb4e8a9fe
Step 4/4 : EXPOSE 8080
 ---> Running in 5054a949c575
Removing intermediate container 5054a949c575
 ---> b60004d121e5
Successfully built b60004d121e5
Successfully tagged microservice-istio-order:latest
```

* The images should now be available in the Kubernets cluster:

```
[~/microservice-istio/microservice-istio]docker images
REPOSITORY                                TAG                 IMAGE ID            CREATED              SIZE
microservice-istio-order             latest              b60004d121e5        About a minute ago   342MB
microservice-istio-invoicing          latest              287e662e8111        About a minute ago   342MB
microservice-istio-shipping           latest              3af9dd80a8ee        About a minute ago   342MB
microservice-istio-apache            latest              eff5fd508880        About a minute ago   240MB
microservice-istio-postgres          latest              deadbeef8880        About a minute ago   42MB
...
```


## Run the containers

* Make sure that the Istio containers are automatically injected when the pods are started:
`kubectl label namespace default istio-injection=enabled`

* Deploy the infrastructure for the microservices using `kubectl`:
`microservice-kubernetes-demo` :

```
[~/microservice-istio/microservice-istio]kubectl apply -f infrastructure.yaml
deployment.apps/apache created
deployment.apps/postgres created
service/apache created
service/postgres created
gateway.networking.istio.io/microservice-gateway created
virtualservice.networking.istio.io/apache created
```


* Deploy the microservices using `kubectl`:


```
[~/microservice-istio/microservice-istio]kubectl apply -f microservices.yaml
deployment.apps/invoicing created
deployment.apps/shipping created
deployment.apps/order created
service/invoicing created
service/shipping created
service/order created
virtualservice.networking.istio.io/shipping created
virtualservice.networking.istio.io/invoicing created
virtualservice.networking.istio.io/order created
```

That deploys the images. It creates Pods. Pods might contain one or
many Docker containers. In this case each Pod contains just one
Docker container.

Note: The Postgres installation is very limited i.e. it is not ensured
that data survives restarts or changes in the cluster. However, for a demo
this should be enough and it simplifies the setup.

Also services are created. Services have a clusterwide unique IP
adress and a DNS entry. Service can use many Pods to do load
balancing. To actually view the services:

* Run `kubectl get services` to see all services:

```
[~/microservice-istio/microservice-istio]kubectl get services
NAME         TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
apache       NodePort    10.110.196.4     <none>        80:31475/TCP     4m13s
catalog      NodePort    10.96.45.251     <none>        8080:32300/TCP   4m13s
customer     NodePort    10.101.227.211   <none>        8080:31621/TCP   4m13s
kubernetes   ClusterIP   10.96.0.1        <none>        443/TCP          152m
order        NodePort    10.109.230.95    <none>        8080:32182/TCP   4m13s
```


* Run `kubectl describe services` for more
  details. This also works for pods (`kubectl describe pods`) and
  deployments (`kubectl describe deployments`).

```
[~/microservice-istio/microservice-istio]kubectl describe service order
Name:                     order
Namespace:                default
Labels:                   run=order
                          visualize=true
Annotations:              kubectl.kubernetes.io/last-applied-configuration:
                            {"apiVersion":"v1","kind":"Service","metadata":{"annotations":{},"creationTimestamp":null,"labels":{"run":"order","visualize":"true"},"nam...
Selector:                 run=order,serving=true
Type:                     NodePort
IP:                       10.109.230.95
Port:                     http  8080/TCP
TargetPort:               8080/TCP
NodePort:                 http  32182/TCP
Endpoints:                172.17.0.20:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>kubectl describe services
```

* You can also get a list of the pods:

```
[~/microservice-istio/microservice-istio]kubectl get pods
NAME                        READY   STATUS    RESTARTS   AGE
apache-64f9b9545d-f5t6c     2/2     Running   0          5m12s
catalog-668455c696-5qf6q    2/2     Running   0          5m12s
customer-7bd99568cd-brsnk   2/2     Running   0          5m12s
order-67d6c6f756-5l2jb      2/2     Running   0          5m12s
```

* ...and you can see the logs of a pod:

```
wolff@BLACK-HARDWARE:~/win/microservice-istio/microservice-istio$ kubectl logs catalog-668455c696-5qf6q catalog
2019-01-07 15:14:00.932  INFO [-,,,] 7 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5c6648b0: startup date [Mon Jan 07 15:14:00 UTC 2019]; root of context hierarchy
2019-01-07 15:14:02.440  INFO [-,,,] 7 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'configurationPropertiesRebinderAutoConfiguration' of type [org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration$$EnhancerBySpringCGLIB$$c87046ec] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.6.RELEASE)

2019-01-07 15:14:03.480  INFO [catalog,,,] 7 --- [           main] c.e.microservice.catalog.CatalogApp      : No active profile set, falling back to default profiles: default
2019-01-07 15:14:03.535  INFO [catalog,,,] 7 --- [           main] ConfigServletWebServerApplicationContext : Refreshing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@57c03d88: startup date [Mon Jan 07 15:14:03 UTC 2019]; parent: org.springframework.context.annotation.AnnotationConfigApplicationContext@5c6648b0
...
2019-01-07 15:14:50.636  INFO [catalog,,,] 7 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Located MBean 'dataSource': registering with JMX server as MBean [com.zaxxer.hikari:name=dataSource,type=HikariDataSource]
2019-01-07 15:14:50.923  INFO [catalog,,,] 7 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2019-01-07 15:14:50.981  INFO [catalog,,,] 7 --- [           main] c.e.microservice.catalog.CatalogApp      : Started CatalogApp in 55.296 seconds (JVM running for 57.545)
...
```

* You can also run commands in a pod:

```
[~/microservice-istio/microservice-istio]kubectl exec catalog-668455c696-5qf6q /bin/ls
Defaulting container name to catalog.
Use 'kubectl describe pod/catalog-668455c696-5qf6q -n default' to see all of the containers in this pod.
bin
boot
dev
docker-java-home
etc
home
lib
lib32
lib64
libx32
media
microservice-istio-shipping-0.0.1-SNAPSHOT.jar
mnt
opt
proc
root
run
sbin
srv
sys
tmp
usr
var
```

* You can even open a shell in a pod:

```
[~/microservice-istio/microservice-istio]kubectl exec catalog-668455c696-5qf6q -it /bin/sh
Defaulting container name to catalog.
Use 'kubectl describe pod/catalog-668455c696-5qf6q -n default' to see all of the containers in this pod.
# ls
bin   docker-java-home  lib    libx32                                              mnt   root  srv  usr
boot  etc               lib32  media                                               opt   run   sys  var
dev   home              lib64  microservice-istio-shipping-0.0.1-SNAPSHOT.jar  proc  sbin  tmp
# 
```

## Use the Demo

The demo is available via an Ingress that provides access to all the
services.

* Make sure the Ingress gatway works:

```
[~/microservice-istio/microservice-istio] kubectl get gateway
NAME                   AGE
microservice-gateway   30m
```


* First figure out the port of the Ingress with `kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}'`. In this case it's `31380`:

```
[~/microservice-istio/microservice-istio]kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}'
31380
```

* Then figure out the IP address of the Minikube VM with `minikube ip`, in this case
`192.168.99.104`:

```
[~/microservice-istio/microservice-istio]minikube ip -p istio2
192.168.99.104
```

* You can also run `ingress-url.sh` which contains this two commands.

* So the Ingress is accessible at http://192.168.99.104:31380/ . If you open
that URL, a static HTML page is shown which is served by the apache service.
This page has links to all other services.

## Prometheus

Istio comes with an installation of [Prometheus](https://prometheus.io/). It collects metrics from the
proxies of the services. This is the foundation for monitoring.

Enter `kubectl -n istio-system port-forward deployment/prometheus 9090:9090` to create a proxy for the Prometheus service. 

You can access Prometheus at http://localhost:9090/ then.

You can also use the shell script `monitoring-prometheus.sh`.

## Grafana

Prometheus has only very limited dashboards. Therefore Istio comes with an installation of [Grafana](https://grafana.com/) that provides much better graphs and dashboards.

Enter `kubectl -n istio-system port-forward deployment/grafana 3000:3000` to create a proxy for the Grafana service. You can access the service at http://localhost:3000/ then. There are quite a few predefined dashboards.

You can also use the shell script `monitoring-grafana.sh`.

## Tracing

[Jaeger](https://www.jaegertracing.io/) is a system to trace calls between microservices.
Istio provides an installation of Jaeger. If you run `kubectl -n istio-system port-forward deployment/istio-tracing 16686:16686` you can access it at http://localhost:16686/ .

You can also use the shell script `tracing.sh`.

## Logging

Istio also supports logging. However, the Istio's logs only contain
information about HTTP request. That is valuable but often not enough
to understand the problems in the microservices. Therefore the
microservices need to log some information. To support a large number
of microservices and to make sure that restarts etc. won't influence
the system, the logs must be stored in a centralized system.

The demo uses
[Elasticsearch](https://www.elastic.co/products/elasticsearch) to
store the logs. Microservices write their logs directly to
Elasticsearchs. [Kibana](https://www.elastic.co/products/kibana) is
used to display and analyze the logs.

To use the log infrastructure, it must be started with `kubectl apply
-f logging.yaml`.


`kubectl -n logging port-forward deployment/kibana 5601:5601`
starts
a proxy for Kibana so it can be reached at http://localhost:5601/ .
You can also use `kibana.sh`.

If the microservices are started without an Elasticsearch server, they
won't try to access it again after the startup. You might therefore
need to restart all microservices by doing `kubectl delete -f
microservices.yaml` and `kubectl apply -f microservices.yaml`.

The logging infrastructure can be removed with `kubectl delete -f
logging.yaml`.

## Fault Injection

Instio provides feature to add fault scenarios to the
system. `fault-injection.yaml` adds a 30s delay to REST requests to
the order services. You can add it to the system with `kubectl apply
-f fault-injection.yaml` . If you make the systems poll new
information from the order microservice now, this takes quite a while
and finally ends in an error.

This is useful to simulate an error in the system.

To remove the fault injection again, just use `kubectl delete -f
fault-injection.yaml`.

## Circuit Breaker

Istio provides a circuit breaker. If a system is too slow or
generates errors, it does not make a lot of sense to still send all
requests to the system. Therefore Istio can have a rule that takes the
traffic and triggers a circuit breaker if a certain number of requests
are waiting to be processed.

Use `kubectl apply -f cicuit-breaker.yaml` to activate the rule and
limit the number of concurrent requests so much that it is easy to
overload the system. Then put some load on the system. There is a very
simple shell script that uses cURL to generate some load, so starting
`./load.sh "-X POST http://192.168.99.110:31380/invoicing/poll" &` a
few times should be enough. You will see some HTTP 500 in the
output. If you do additional `curl -X POST
http://192.168.99.110:31380/invoicing/poll`, quite a few of them will
also result in a 500. Use `ingress-url.sh` to figure out the URL.

Use `kubectl apply -f cicuit-breaker.yaml` to remove the rule.

Note that you can also set a timeout so the system won't be waiting
too long for a request to be handled, see
https://istio.io/docs/tasks/traffic-management/request-timeouts/ .

## Clean Up

* To remove all services and deployments run `kubectl  delete -f microservices.yaml`:

```
[~/microservice-istio/microservice-istio]kubectl  delete -f microservices.yaml
deployment.apps "catalog" deleted
deployment.apps "customer" deleted
deployment.apps "order" deleted
service "catalog" deleted
service "customer" deleted
service "order" deleted
virtualservice.networking.istio.io "customer" deleted
virtualservice.networking.istio.io "catalog" deleted
virtualservice.networking.istio.io "order" deleted
```

* Then remove the infrastructure - run `kubectl  delete -f
  infrastructure.yaml`:

```
[~/microservice-istio/microservice-istio]kubectl  delete -f infrastructure.yaml
deployment.apps "apache" deleted
service "apache" deleted
gateway.networking.istio.io "microservice-gateway" deleted
virtualservice.networking.istio.io "apache" deleted
```
