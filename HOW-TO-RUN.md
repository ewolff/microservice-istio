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
  depending on your local machine. Please make sure that you deleted any pre-existing 
  minikube instance using `minikube delete` as the memory and cpu values would otherwise have no effect. 

```
[~/microservice-istio]minikube start --cpus=2 --memory=6000
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

* Install [kubectl](https://kubernetes.io/docs/tasks/kubectl/install/). This
  is the command line interface for Kubernetes.

* [Download](https://istio.io/docs/setup/kubernetes/download-release/) and [install](https://istio.io/docs/setup/kubernetes/quick-start/) Istio.
It is enough to install Istio *without* mutual TLS authentication
between sidecars.

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

Change to the directory `microservice-istio-demo` and run `./mvnw clean
package` (macOS / Linux) or `mvnw.cmd clean package` (Windows). This will take a while:

```
[~/microservice-istio/microservice-istio-demo]./mvnw clean package
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

* Configure Docker so that it uses the Kubernetes cluster. This is
required to install the
Docker images: `minikube docker-env`(macOS / Linux) or `minikube.exe docker-env`(Windows) tells you how to do that. 

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
`microservice-istio-demo`. It builds the images and uploads them into the
Kubernetes cluster.

```
[~/microservice-istio/microservice-istio-demo]./docker-build.sh 
...
Successfully tagged microservice-istio-invoicing:latest
Sending build context to Docker daemon  47.88MB
Step 1/4 : FROM openjdk:11.0.2-jre-slim
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
[~/microservice-istio/microservice-istio-demo]docker images
REPOSITORY                                TAG                 IMAGE ID            CREATED              SIZE
microservice-istio-order             latest              b60004d121e5        About a minute ago   342MB
microservice-istio-invoicing          latest              287e662e8111        About a minute ago   342MB
microservice-istio-shipping           latest              3af9dd80a8ee        About a minute ago   342MB
microservice-istio-apache            latest              eff5fd508880        About a minute ago   240MB
microservice-istio-postgres          latest              deadbeef8880        About a minute ago   42MB
...
```


## Run the Containers

* Make sure that the Istio containers are automatically injected when the pods are started:
`kubectl label namespace default istio-injection=enabled`

* Deploy the infrastructure for the microservices using `kubectl` in
the directory
`microservice-kubernetes-demo` :

```
[~/microservice-istio/microservice-istio-demo]kubectl apply -f infrastructure.yaml
deployment.apps/apache created
deployment.apps/postgres created
service/apache created
service/postgres created
gateway.networking.istio.io/microservice-gateway created
virtualservice.networking.istio.io/apache created
```


* Deploy the microservices using `kubectl`:


```
[~/microservice-istio/microservice-istio-demo]kubectl apply -f microservices.yaml
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

The script creates Pods based on the Docker images created
before. Pods might
contain one or
many Docker containers. In this case, each Pod contains one
Docker container with the microservice and another one with Istio
infrastructure is created automatically.

Note: The Postgres installation is very limited i.e. it is not ensured
that data survives restarts or changes in the cluster. However, for a demo
this should be enough and it simplifies the setup.

Also Kubernetes services are created. Services have a clusterwide unique IP
address and a DNS entry. Service can use many Pods to do load
balancing. To actually view the services:

* Run `kubectl get services` to see all services:

```
[~/microservice-istio/microservice-istio-demo]kubectl get services
NAME         TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
apache       NodePort    10.110.196.4     <none>        80:31475/TCP     4m13s
catalog      NodePort    10.96.45.251     <none>        8080:32300/TCP   4m13s
customer     NodePort    10.101.227.211   <none>        8080:31621/TCP   4m13s
kubernetes   ClusterIP   10.96.0.1        <none>        443/TCP          152m
order        NodePort    10.109.230.95    <none>        8080:32182/TCP   4m13s
```


* Run `kubectl describe service` for more
  details. This also works for pods (`kubectl describe pod`) and
  deployments (`kubectl describe deployment`).

```
[~/microservice-istio/microservice-istio-demo]kubectl describe service order
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
[~/microservice-istio/microservice-istio-demo]kubectl get pods
NAME                        READY   STATUS    RESTARTS   AGE
apache-7f7f7f79c6-jbqx8      2/2     Running   0          8m51s
invoicing-77f69ff854-rpcbk   2/2     Running   0          8m43s
order-cc7f8866-9zbnf         2/2     Running   0          8m43s
postgres-5ddddbbf8f-xfng5    2/2     Running   0          8m51s
shipping-5d58798cdd-9jqj8    2/2     Running   0          8m43s
```

* ...and you can see the logs of a pod:

```
~/microservice-istio/microservice-istio-demo$ kubectl logs order-cc7f8866-9zbnf order 

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.6.RELEASE)

2019-01-17 16:56:30.909  INFO 6 --- [           main] com.ewolff.microservice.order.OrderApp   : Starting OrderApp v0.0.1-SNAPSHOT on order-cc7f8866-9zbnf with PID 6 (/microservice-istio-order-0.0.1-SNAPSHOT.jar started by root in /)
2019-01-17 16:56:30.918  INFO 6 --- [           main] com.ewolff.microservice.order.OrderApp   : No active profile set, falling back to default profiles: default
2019-01-17 16:56:31.200  INFO 6 --- [    es-writer-1] es-logger                                : {"index":{"_index":"logs-2019-01-17","_type":"tester"}}
{"@timestamp":"2019-01-17T16:56:30.909+0000","message":"Starting OrderApp v0.0.1-SNAPSHOT on order-cc7f8866-9zbnf with PID 6 (/microservice-istio-order-0.0.1-SNAPSHOT.jar started by root in /)","host":"order-cc7f8866-9zbnf","severity":"INFO","thread":"main","logger":"com.ewolff.microservice.order.OrderApp"}
{"index":{"_index":"logs-2019-01-17","_type":"tester"}}
{"@timestamp":"2019-01-17T16:56:30.918+0000","message":"No active profile set, falling back to default profiles: default","host":"order-cc7f8866-9zbnf","severity":"INFO","thread":"main","logger":"com.ewolff.microservice.order.OrderApp"}
...
```

* You can also run commands in a pod:

```
[~/microservice-istio/microservice-istio-demo]kubectl exec order-cc7f8866-9zbnf /bin/ls
Defaulting container name to order.
Use 'kubectl describe pod/order-cc7f8866-9zbnf -n default' to see all of the containers in this pod.
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
microservice-istio-order-0.0.1-SNAPSHOT.jar
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
[~/microservice-istio/microservice-istio-demo]kubectl exec order-cc7f8866-9zbnf -it /bin/sh
Defaulting container name to order.
Use 'kubectl describe pod/order-cc7f8866-9zbnf -n default' to see all of the containers in this pod.
# ls
bin  boot  dev	docker-java-home  etc  home  lib  lib32  lib64	libx32	media  microservice-istio-order-0.0.1-SNAPSHOT.jar  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var
#
```

## Use the Demo

The demo is available via an Ingress that provides access to all the
services.

* Make sure the Ingress gateway works:

```
[~/microservice-istio/microservice-istio-demo] kubectl get gateway
NAME                   AGE
microservice-gateway   30m
```


* First figure out the port of the Ingress with `kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}'`. In this case it's `31380`:

```
[~/microservice-istio/microservice-istio-demo]kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}'
31380
```

* Then figure out the IP address of the Minikube VM with `minikube ip`, in this case
`192.168.99.104`:

```
[~/microservice-istio/microservice-istio-demo]minikube ip
192.168.99.104
```

* You can also run `ingress-url.sh` which contains this two commands.

* So the Ingress is accessible at http://192.168.99.104:31380/ . If you open
that URL, a static HTML page is shown which is served by the apache service.
This page has links to all other services.

## Adding another Microservice

There is another microservice in the sub directory
`microservice-istio-bonus`. To add the microservice to your system you
can do the following:

* Change to the directory `microservice-istio-bonus` and run `./mvnw clean
package` (macOS / Linux) or `mvnw.cmd clean package` (Windows) to
compile the Java 
code.

* Run `docker-build.sh` in the directory
`microservice-istio-bonus`. It builds the Docker images and uploads them into
the Kubernetes cluster.

* Deploy the microservice with `kubectl apply -f bonus.yaml`.

* You can remove the microservice again with `kubectl delete -f bonus.yaml`.

## Adding a Microservice with Helm

You can also add the microservice with Helm.  [Helm](https://helm.sh/)
is a package manager for Kubernetes that supports templates for
Kubernetes configurations. That makes it easier to add a new
microservice:

* First you need to [install
  Helm](https://docs.helm.sh/using_helm/#installing-helm) into the
  Kubernetes cluster.
  
* The directory `spring-boot-microservice` contains a Helm chart for
  the microservices in this project. So `helm install --set name=bonus
  spring-boot-microservice/` is enough to deploy the microservice.
  
```
[~/microservice-istio] helm install --set name=bonus  spring-boot-microservice/
NAME:   waxen-newt
LAST DEPLOYED: Wed Feb 13 14:25:52 2019
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/Service
NAME   TYPE      CLUSTER-IP     EXTERNAL-IP  PORT(S)         AGE
bonus  NodePort  10.108.31.211  <none>       8080:30878/TCP  5s

==> v1beta1/Deployment
NAME   DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
bonus  1        1        1           1          5s

==> v1alpha3/VirtualService
NAME   AGE
bonus  1s

==> v1/Pod(related)
NAME                    READY  STATUS   RESTARTS  AGE
bonus-55d854b9d9-sn4pm  2/2    Running  0         4s
```

* A name is automatically assigned to the Helm release. In this example,
  the name is `waxen-newt`. `helm list` provides a list of releases
  that are currently installed.

* You can remove the Helm release by using this assigned name
  e.g. `helm delete waxen-newt`.

#### Using the additonal microservice

The bonus microservice is not included in the static web page that
contains links to the other microservices. However, it can be accessed
via the Ingress gateway. If the Ingress gateway's URL is
http://192.168.99.127:31380/, you can access the bonus microservice
at http://192.168.99.127:31380/bonus.

Note that the bonus microservice does not show any revenue for the
orders. This is because it requires a field `revenue` in the data the
order microservice provides. That field is currently not included in
the data structure. This shows that adding a new microservice might
require changes to a common data structure. Such changes might also
impact the other microservices.

## Prometheus

Istio comes with an installation of [Prometheus](https://prometheus.io/). It collects metrics from the
proxies of the services. This is the foundation for monitoring.

Enter `kubectl -n istio-system port-forward deployment/prometheus 9090:9090` to create a proxy for the Prometheus service. 

You can access Prometheus at http://localhost:9090/ then.

You can also use the shell script `monitoring-prometheus.sh`.

## Grafana

Prometheus has only very limited dashboards. Therefore Istio comes with an installation of [Grafana](https://grafana.com/) that provides much better graphs and dashboards.

Enter `kubectl -n istio-system port-forward deployment/grafana
3000:3000` to create a proxy for the Grafana service. You can access
the tool at http://localhost:3000/ then. There are quite a few predefined dashboards.

You can also use the shell script `monitoring-grafana.sh`.

## Tracing

[Jaeger](https://www.jaegertracing.io/) is a system to trace calls between microservices.
Istio provides an installation of Jaeger. If you run `kubectl -n
istio-system port-forward deployment/istio-tracing 16686:16686` you
can access it at http://localhost:16686/ .

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
Elasticsearch. [Kibana](https://www.elastic.co/products/kibana) is
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

The log infrastructure can be removed with `kubectl delete -f
logging.yaml`.

## Fault Injection

Istio provides feature to add fault scenarios to the
system. That makes it possible to test the system's resilience.
`fault-injection.yaml` adds a 30s delay to REST requests to
the order microservice. You can add it to the system with `kubectl apply
-f fault-injection.yaml` . If you make the shipping or invoicing
microservices poll new
information from the order microservice now, this takes quite a while
and finally ends in an error.

To remove the fault injection again, just use `kubectl delete -f
fault-injection.yaml`.

## Circuit Breaker

Istio provides a circuit breaker. If a system is too slow or
generates errors, it does not make a lot of sense to still send all
requests to the system. Therefore Istio can have a rule that triggers
a circuit breaker if a certain number of requests
are waiting to be processed or run into errors.
In that case the requests are no longer forwarded to the system but an
error is returned.

Use `kubectl apply -f cicuit-breaker.yaml` to activate the rule and
limit the number of concurrent requests so much that it is easy to
overload the system. Then put some load on the system. There is a very
simple shell script that uses cURL to generate some load, so starting
`./load.sh "-X POST http://192.168.99.110:31380/invoicing/poll" &` a
few times should be enough. You will see some HTTP 500 in the
output that the circuit breaker has caused. If you do additional `curl -X POST
http://192.168.99.110:31380/invoicing/poll`, quite a few of them will
also result in a 500. Use `ingress-url.sh` to figure out the URL.

Use `kubectl apply -f cicuit-breaker.yaml` to remove the rule.

Note that you can also set a timeout so the system won't be waiting
too long for a request to be handled, see
https://istio.io/docs/tasks/traffic-management/request-timeouts/ .

## Retry

Use `kubectl apply -f failing-order-service.yaml` to deploy a version
of the order microservice that answers 50% of all requests with an
http status code of 500. If you access the order microservice's web UI
or if you make shipping and invoicing poll the order microservice, you
will likely receive an error.

With `kubectl apply -f retry.yaml` you can make Istio retry requests
to the order service. The configuration adds retries to the
communication between the microservices as well as the access through
the Ingress gateway. So polling and the web UI will both work again.

## Clean Up

* To remove all services and deployments run `kubectl  delete -f microservices.yaml`:

```
[~/microservice-istio/microservice-istio-demo]kubectl  delete -f microservices.yaml
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
[~/microservice-istio/microservice-istio-demo]kubectl  delete -f infrastructure.yaml
deployment.apps "apache" deleted
service "apache" deleted
gateway.networking.istio.io "microservice-gateway" deleted
virtualservice.networking.istio.io "apache" deleted
```

## Installation with Helm

The Kubernetes configuration for the microservices are very
similar. It therefore makes sense to use a template and parameterize
it. [Helm](https://helm.sh/) is a tool to create such template and use
them to deploy Kubernetes systems. The templates are called Helm Charts.

* Install Helm, see
https://github.com/helm/helm/blob/master/docs/install.md

* To actually use Helm on the Kubernetes cluster, you need to run
  `helm init`.
  
* Make sure that the infrastructure is deployed with `kubectl apply -f
  infrastructure.yaml`, see above.

* To install one of the microservices `order`, `shipping`, and
  `invoicing` you just need to run `helm install --set name=order
  ../spring-boot-microservice/`. `../spring-boot-microservice` is the
  directory that contains the Helm Chart.
  
* The file `spring-boot-microservice/values.yaml` contains the other
  values that can be changed for an installation just like `name`.

* `helm install --dry-run --set name=order
  ../spring-boot-microservice/`. `../spring-boot-microservice` does a
  dry run i.e. nothing is actually changed. Just the logs are shown.

* You can also use the shell scritp `install-helm.sh` that contains
all the necessary `helm`commands to run all three microservices:

```
[~/microservice-istio/microservice-istio-demo]./install-helm.sh
NAME:   wobbling-billygoat
LAST DEPLOYED: Wed Jan 16 10:07:50 2019
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/Pod(related)
NAME                    READY  STATUS   RESTARTS  AGE
order-79cd6c8844-vhcqj  0/2    Pending  0         0s

==> v1/Service
NAME   TYPE      CLUSTER-IP      EXTERNAL-IP  PORT(S)         AGE
order  NodePort  10.109.102.171  <none>       8080:31666/TCP  0s

==> v1beta1/Deployment
NAME   DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
order  1        1        1           0          0s
...
```

* The result are multiple releases of the Helm Chart with
  different parameters. The one in the console output above is called
  `wobbling-billygoat` for example. That name is automatically generated.


## Clean-Up Helm installation

* First you need to figure out the names of the Helm releases:

```
[~/microservice-istio/microservice-istio-demo] helm list
NAME            REVISION        UPDATED                         STATUS          CHART                           APP VERSION                                          NAMESPACE
flabby-abalone  1               Wed Jan 16 08:21:50 2019        DEPLOYED        spring-boot-microservice-0.1.0  1.0                                                  default
insipid-puma    1               Wed Jan 16 08:21:57 2019        DEPLOYED        spring-boot-microservice-0.1.0  1.0                                                  default
lame-skunk      1               Wed Jan 16 08:21:40 2019        DEPLOYED        spring-boot-microservice-0.1.0  1.0                                                  default
``` 

* Then you can delete by name:

``` 
[~/microservice-istio/microservice-istio-demo] helm delete flabby-abalone
release "flabby-abalone" deleted
[~/microservice-istio/microservice-istio-demo] helm delete insipid-puma
,release "insipid-puma" deleted
[~/microservice-istio/microservice-istio-demo] helm delete lame-skunk
release "lame-skunk" deleted
``` 


* You can also remove the infrastructure with `kubectl delete -f
  infrastructure.yaml`.
