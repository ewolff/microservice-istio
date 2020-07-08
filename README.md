Microservice Istio Sample
=====================

[Deutsche Anleitung zum Starten des Beispiels](WIE-LAUFEN.md)

This demo uses [Kubernetes](https://kubernetes.io/) as Docker
environment. Kubernetes also support service discovery and load
balancing. An Apache httpd as a reverse proxy routes the calls to the
services.

Also the demo uses [Istio](https://istio.io/) for features like
monitoring, tracing, fault injection, and circuit breaking.

This project creates a complete microservice demo system in Docker
containers. The services are implemented in Java using Spring Boot and
Spring Cloud.


It uses three microservices:
- `Order` to accept orders.
- `Shipping` to ship the orders.
- `Invoicing` to ship invoices.

How to run
---------

See [How to run](HOW-TO-RUN.md).


Remarks on the Code
-------------------

The microservices are: 
- [microservice-istio-order](microservice-istio-demo/microservice-istio-order) to create the orders
- [microserivce-istio-shipping](microservice-istio-demo/microservice-istio-shipping) for the shipping
- [microservice-istio-invoicing](microservice-istio-demo/microservice-istio-invoicing) for the invoices

The microservices have an Java main application in `src/test/java` to
run them stand alone. microservice-demo-shipping and
microservice-demo-invoicing both use a stub for the
other order service for the tests.

The data of an order is copied - including the data of the customer
and the items. So if a customer or item changes in the order system
this does not influence existing shipments and invoices. It would be
odd if a change to a price would also change existing invoices. Also
only the information needed for the shipment and the invoice are
copied over to the other systems.

The job to poll the order feed is run every 30 seconds.

