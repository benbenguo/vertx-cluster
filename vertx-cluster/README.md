# vertx-cluster

This is a tutorial project for studying clustered vertx. The cluster is based on Hazelcast, The vertx-cluster is include four modules. as follows

- http-verticle module
> This module is vertx http verticle, receive http request, and request job (job-verticle application in cluster), then reponse to client.
- web-verticle module
> This module is vertx http web verticle, receive http request, and request job (job-verticle application in cluster), then reponse to client. Different from http-verticle, web verticle is include vertx-web's session.
- job-verticle
> This module is vertx eventbus' consumer, business application. It receive http-verticle and web-verticle send job request.
- vertx-service
> This module service for the other modules, include common function.
