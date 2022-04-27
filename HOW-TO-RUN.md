# How to Run

This is a step-by-step guide how to run the example:

## Installation Minikube

* Install
[minikube](https://github.com/kubernetes/minikube/releases). Minikube
is a Kubernetes environment in a virtual machine that is easy to use
and install. It is not meant for production but to test Kubernetes or
for developer environments.

* Create a Minikube instance with `minikube start --cpus=2 --memory=5000`. This
  will set the memory of the Kubernetes VM to 6.000 MB - which should
  be enough for most experiments. You might want to adjust the number of CPUs
  depending on your local machine. Please make sure that you deleted any pre-existing 
  minikube instance using `minikube delete` as the memory and cpu values would otherwise have no effect. 

```
[~/microservice-istio]minikube start --cpus=2 --memory=5000
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

## Installation Google Cloud

* Go to the [Kubernetes Engine Page](https://console.cloud.google.com/projectselector/kubernetes?_ga=2.66966445.-2058400183.1547494992)

* Create or select a project for this demo.

Note: You can do the installation and all other steps in the [Google
Cloud Shell](https://cloud.google.com/shell/docs/). The Google Cloud
Shell provides access to a Linux system. That way there is no need to
install any software on the local machine, just a modern web browser
is enough.

Otherwise you need to install the [Google Cloud
SDK](https://cloud.google.com/sdk/docs/quickstarts) and
[kubectl](https://kubernetes.io/docs/tasks/kubectl/install/).

* Log in to the Google Cloug `gcloud auth login <email address>`

* Select the project from the Kubernetes Engine Page with `gcloud
  config set project <project name>`

* Choose a data center e.g. in Frankfurt `gcloud config set
  compute/zone europe-west3-a`
  
* Configure Docker `gcloud auth configure-docker`

* Create a cluster with `gcloud container clusters create
  hello-cluster --num-nodes=2 --release-channel=rapid`
    
## Install Istio

This and all following steps are either done in the command line
(Minikube / Google Cloud) or the Google Cloud Shell.

* [Install](https://istio.io/docs/setup/getting-started/) istioctl and use it install istio on the cluster. Use the demo profile. 

* Also install the addons (Kiali, Prometheus, Jaeger, Grafana). In the
  subdirectory `samples/addons` of the istio configuration are
  matching configuration files. Apply them with `kubectl apply -f
  samples/addons`.

## Build the Docker images

This step is optional. You can skip this part and
proceed to "Run the Containers".

* The example is implemented in Java. See
   https://www.java.com/en/download/help/download_options.xml about how
   to download Java. The
   examples need to be compiled so you need to install a JDK (Java
   Development Kit). A JRE (Java Runtime Environment) is not
   sufficient. After the installation you should be able to execute
   `java` and `javac` on the command line.
   You need at least Java 10. In the Google Cloud Shell, use `sudo
   update-java-alternatives -s java-1.11.0-openjdk-amd64 && export
   JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64` to select Java 11.

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

Now the Java code has been compiled. The next step is to create Docker
images:

* Minkube only: Configure Docker so that it uses the Kubernetes cluster. This is
required to install the
Docker images: `minikube docker-env`(macOS / Linux) or `minikube.exe docker-env`(Windows) tells you how to do that. 

* Minikube only: Afterwards you should see the Docker images of Kubernetes if you do `docker images`:

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
`microservice-istio-demo`. It builds the Docker images.

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

* Sometime pulling the images does not work. Try `docker logout` then
  and rerun the script.

* The images should now be available:

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

* Google Cloud only: Upload the images to the Google Cloud with `./docker-push-gcp.sh`


## Run the Containers

* Make sure that the Istio containers are automatically injected when the pods are started:
`kubectl label namespace default istio-injection=enabled`
* If you have build the Docker images yourself and use Google Cloud: 
  Modify the YAML files to load the Docker images
from the Google Docker repo with `fix-microservices-gcp.sh`
* Deploy the infrastructure for the microservices using `kubectl` in
  the directory
  `microservice-kubernetes-demo`.
  Use `infrastructure-gcp.yaml` instead of `infrastructure.yaml` if you
  if you built and uploaded the images to Google Cloud. 
* Use `infrastructure-dockerhub.yaml` if you haven't
  built the Docker container yourself.
  The Docker images will be
  downloaded from the Docker Hub in the Internet in that case.:

```
[~/microservice-istio/microservice-istio-demo]kubectl apply -f infrastructure.yaml
deployment.apps/apache created
deployment.apps/postgres created
service/apache created
service/postgres created
gateway.networking.istio.io/microservice-gateway created
virtualservice.networking.istio.io/apache created
```


* Deploy the microservices using `kubectl`.
  Use `microservices-gcp.yaml` instead of `microservices.yaml` if you
  if you built and uploaded the images to Google Cloud. 
  Use `microservices-dockerhub.yaml` if you haven't
  built the Docker container yourself:

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
apache       NodePort    10.31.244.219   <none>        80:30798/TCP     29m
invoicing    NodePort    10.31.249.73    <none>        80:32105/TCP     2m34s
kubernetes   ClusterIP   10.31.240.1     <none>        443/TCP          80m
order        NodePort    10.31.241.103   <none>        80:30426/TCP     2m33s
postgres     NodePort    10.31.252.0     <none>        5432:31520/TCP   29m
shipping     NodePort    10.31.251.213   <none>        80:31754/TCP     2m34s
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

## Build and Run the Native Images

Spring Boot now provides the option to build native images. GraalVM is
used to compile the code ahead of time. Native images start much
quicker than JVM based images.

* You can build the native images on your local machine with
  `docker-build-native.sh`. Please note that this takes quite a
  while. Also you should have 16GB RAM to run the build process
  successfully. If you don't want to build the images yourself, you
  can download them from Dockerhub - see below.

* To run the images, use `kubectl apply -f microservices-native.yaml`.

* If you want to run the images from Dockerhub, use `kubectl apply -f
  microservices-native-dockerhub.yaml`.

## Use the Demo

The demo is available via an Ingress that provides access to all the
services.

* Make sure the Ingress gateway works:

```
[~/microservice-istio/microservice-istio-demo] kubectl get gateway
NAME                   AGE
microservice-gateway   30m
```

* `ingress-url.sh` outputs the URL for the Ingress
  gateway for minikube. For the Google Cloud, use `ingress-gcp.sh`

* If you open
the Ingress URL, a static HTML page is shown which is served by the apache service.
This page has links to all other services.

* If it is not possible to access the Ingress or if the scripts show
  no valid URL, you can still use the script `ingress-forward.sh`. It
  creates a proxy to the Ingress on the local machine. The script
  prints out the URL for the proxy.

## Adding another Microservice

There is another microservice in the sub directory
`microservice-istio-bonus`.
This microservice shows how you can add another microservice to the
system without sharing the build infrastructure. So for example you
can change the Java version or Spring Boot version of this
microservice without any impact on the other microservices.
To add the microservice to your system you
can do the following:

* Change to the directory `microservice-istio-bonus` and run `./mvnw clean
package` (macOS / Linux) or `mvnw.cmd clean package` (Windows) to
compile the Java 
code.

* Google Cloud only: Upload the Docker images with `docker-push-gcp.sh`.

* Run `docker-build.sh` in the directory
`microservice-istio-bonus`. It builds the Docker images and uploads them into
the Kubernetes cluster.

* Deploy the microservice with `kubectl apply -f bonus.yaml`.

* Google Cloud: Use `fix-bonus-gcp.sh` first and then deploy with
  `kubectl apply -f bonus-gcp.yaml`.

You can also download the images from Dockerhub with
`kubectl apply -f
bonus-dockerhub.yaml`. That way there is no need to build them
locally.

You can remove the microservice again with `kubectl delete -f bonus.yaml`.

## Adding another Microservice as a Native Image

There is also a version of the bonus microservice as a native
image. In that case, the Java application is compiled ahead of time
using the GraalVM.

* You can build it with `mvn -B spring-boot:build-image --file
  pom.xml` .
  
* You can run the native image with `kubectl apply -f
  bonus-native.yaml`.
  
* If you want to download the image from Dockerhub, there is no need
  to build it. You can just go ahead and run it with `kubectl apply -f
  bonus-native-dockerhub.yaml`

## Adding a Microservice with Helm

You can also add the microservice with Helm.  [Helm](https://helm.sh/)
is a package manager for Kubernetes that supports templates for
Kubernetes configurations. That makes it easier to add a new
microservice:

* First you need to [install
  Helm 3](https://helm.sh/docs/intro/install/)
  
* The directory `spring-boot-microservice` contains a Helm chart for
  the microservices in this project. So `helm install bonus --set name=bonus spring-boot-microservice/` is enough to deploy the microservice.
```
[~/microservice-istio] helm install bonus --set name=bonus spring-boot-microservice/
NAME: bonus
LAST DEPLOYED: Thu Apr 30 13:38:08 2020
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
```

*  `helm list` provides a list of releases
  that are currently installed.
  
* You can remove the Helm release by using this assigned name
  e.g. `helm delete bonus`.

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
In the Google Cloud Shell you can use [Web
Preview](https://cloud.google.com/shell/docs/using-web-preview) to
open the URL from the shell in your local browser.

You can also use  `istioctl dashboard prometheus`.

Metrics only make sense if there is load on the system. The shell
script `load.sh` uses the tool `curl` to request a certain URL 1,000
times. You can start one or multiple instances of this script with the
URL of the home page of the shipping microservice to create some
load on that service.

## Grafana

Prometheus has only very limited dashboards. Therefore Istio comes with an installation of [Grafana](https://grafana.com/) that provides much better graphs and dashboards.

Enter `kubectl -n istio-system port-forward deployment/grafana
3000:3000` to create a proxy for the Grafana service. You can access
the tool at http://localhost:3000/ then. There are quite a few predefined dashboards. 
In case they are not listed at the start page, go to Dashboard (4 tiles symbol) --> Manage. 

You can also use `istioctl dashboard grafana`.

## Tracing

[Jaeger](https://www.jaegertracing.io/) is a system to trace calls between microservices.
Istio provides an installation of Jaeger. If you run `kubectl -n
istio-system port-forward deployment/istio-tracing 16686:16686` you
can access it at http://localhost:16686/ .

You can also use `istioctl dashboard jaeger`.

## Kiali

[Kiali](https://www.kiali.io/) is a tool to visualize the dependencies
between microservices. It generates a graph and also adds some basic
metrics to the graph. You can use run `kubectl -n istio-system port-forward deployment/kiali 20001:20001` or `istioctl dashboard kiali` to start
a proxy to Istio's Kiali installation. Then you can open the Kiali
console at http://localhost:20001/ . For the default installation, the
user name is admin and the password is admin, too.

## Logging

Istio also [supports logging](https://istio.io/docs/tasks/observability/logs/access-log/). However, the Istio's logs only contain
information about HTTP request. That is valuable but often not enough
to understand the problems in the microservices. Therefore the
microservices need to log some information. To support a large number
of microservices and to make sure that restarts etc. won't influence
the system, the logs must be stored in a centralized system.

## Security

Istio upgrades connections to mTLS by default where possible. Kiali visualizes 
if a connection is mutually authenticated when Display --> Security is checked.

Istio does however not enforce mTLS by default. The current authentication 
policy for a pod can be displayed using  `istioctl authn tls-check <pod-id>`.

The result for the order-pod shows that no policy is active for the default namespace

```sh
[~/microservice-istio/microservice-istio-demo] ORDER_POD=$(kubectl get pod -l app=order -o jsonpath="{.items[0].metadata.name}") 
[~/microservice-istio/microservice-istio-demo] istioctl authn tls-check $ORDER_POD | grep "default.svc\|HOST:PORT"
HOST:PORT                                                          STATUS     SERVER      CLIENT           AUTHN POLICY                                 DESTINATION RULE
apache.default.svc.cluster.local:80                                AUTO       DISABLE     -                None                                         -
invoicing.default.svc.cluster.local:80                             AUTO       DISABLE     -                None                                         -
kubernetes.default.svc.cluster.local:443                           AUTO       DISABLE     -                None                                         -
...
```

Add a Authentication `Policy` and explicitly enable mTLS by adding a `DestinationRule` for the invoicing service by executing `kubectl apply -f enforce-mtls.yaml`.

You see that now mTLS is enforced between order and invoicing.

```
[~/microservice-istio/microservice-istio-demo] istioctl authn tls-check $ORDER_POD | grep "default.svc\|HOST:PORT"
HOST:PORT                                                          STATUS     SERVER      CLIENT           AUTHN POLICY                                 DESTINATION RULE
apache.default.svc.cluster.local:80                                AUTO       DISABLE     -                None                                         -
invoicing.default.svc.cluster.local:80                             OK         STRICT      ISTIO_MUTUAL     default/invoicing-policy                     default/invoicing
kubernetes.default.svc.cluster.local:443                           AUTO       DISABLE     -                None                                         -
...
```

Remove mTLS enforcement by running `kubectl delete -f enforce-mtls.yaml`.

## Fault Injection

Istio provides feature to add fault scenarios to the
system. That makes it possible to test the system's resilience.
`fault-injection.yaml` will make all REST requests to
the order microservice fail. You can add it to the system with `kubectl apply
-f fault-injection.yaml` . If you make the shipping or invoicing
microservices poll new
information from the order microservice now, this 
ends in an error.

To remove the fault injection again, just use `kubectl delete -f
fault-injection.yaml`.

## Delay Injection

You can apply a delay to the system with `kubectl apply -f
delay-injection.yaml` and remove it again with `kubectl delete -f
delay-injection.yaml`. This will add a delay of 7s to each call to the
order microservice. If you make the shipping microservice poll the
order microservice, it will take longer now.

## Circuit Breaker

Istio provides a circuit breaker. If a system is too slow or
generates errors, it does not make a lot of sense to still send all
requests to the system. Therefore Istio can have a rule that triggers
a circuit breaker if a certain number of requests
are waiting to be processed or run into errors.
In that case the requests are no longer forwarded to the system but an
error is returned.

Use `kubectl apply -f circuit-breaker.yaml` to activate the rule and
limit the number of concurrent requests so much that it is easy to
overload the system. Then put some load on the system. There is a very
simple shell script that uses cURL to generate some load, so starting
`./load.sh "-X POST http://192.168.99.110:31380/invoicing/poll" &` a
few times should be enough. You will see some HTTP 500 in the
output that the circuit breaker has caused. If you do additional `curl -X POST
http://192.168.99.110:31380/invoicing/poll`, quite a few of them will
also result in a 500. Use `ingress-url.sh` (minikube) or
`ingress-gcp.sh` (Google Cloud) to figure out the URL.

Use `kubectl delete -f circuit-breaker.yaml` to remove the rule.

Note that you can also set a timeout so the system won't be waiting
too long for a request to be handled, see
https://istio.io/docs/tasks/traffic-management/request-timeouts/ .

## Retry

Use `kubectl apply -f failing-order-service.yaml` to deploy a version
of the order microservice that answers 50% of all requests with an
http status code of 500. 
Use `failing-order-service-gcp.yaml` instead of
`failing-order-service.yaml` if you build and uploaded images to Google Cloud. Use
`failing-order-service-dockerhub.yaml` if you haven't built the Docker
container yourself.

If you access the order microservice's web UI
or if you make shipping and invoicing poll the order microservice, you
will likely receive an error.

With `kubectl apply -f retry.yaml` you can make Istio retry requests
to the order service. The configuration adds retries to the
communication between the microservices as well as the access through
the Ingress gateway. So polling and the web UI will both work again.

You can remove the retries with `kubectl delete -f retry.yaml`. The
failing microservice can be set to normal with `kubectl apply -f
microservices.yaml`.

## Clean Up

* To remove all services and deployments run `kubectl  delete -f microservices.yaml`:

```
[~/microservice-istio/microservice-istio-demo] kubectl delete -f microservices.yaml
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
https://github.com/helm/helm#install

* Make sure that the infrastructure is deployed with `kubectl apply -f
  infrastructure.yaml`, see above.

* To install one of the microservices `order`, `shipping`, and
  `invoicing` you just need to run `helm install order --set name=order
  ../spring-boot-microservice/`. `../spring-boot-microservice` is the
  directory that contains the Helm Chart.
  
* The file `spring-boot-microservice/values.yaml` contains the other
  values that can be changed for an installation just like `name`.

* `helm install order --dry-run --set name=order
  ../spring-boot-microservice/`. `../spring-boot-microservice` does a
  dry run i.e. nothing is actually changed. Just the logs are shown.

* You can also use the shell scritp `install-helm.sh` that contains
all the necessary `helm` commands to run all three microservices:

```
[~/microservice-istio/microservice-istio-demo]./install-helm.sh
NAME: order
LAST DEPLOYED: Thu Apr 30 17:56:36 2020
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
...
```

* The result are multiple releases of the Helm Chart with
  different parameters. 


## Clean-Up Helm installation

* Delete all three Helm releases by executing `helm delete invoicing order shipping` 

* You can also remove the infrastructure with `kubectl delete -f
  infrastructure.yaml`.
