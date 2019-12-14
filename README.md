# Hazelcast and Quarkus

This guide helps you learn how to use Hazelcast from Quarkus in a containerized environment. 
Feel free to fork it and experiment on your own.

## Requirements

### Docker
Please make sure docker and docker-compose is available in your system. If docker is not installed, you can visit [Docker Getting Started](https://www.docker.com/get-started) website.
```
$ docker -v
Docker version 19.03.5, build 633a0ea
$ docker-compose -v
docker-compose version 1.24.1, build 4667896b
```

## Hazelcast-Quarkus Code Sample

This guide contains a basic Quarkus Microservice with Hazelcast Client Server Topology. 
Business Logic implemented in Microservice is very simple. `put` operation puts a key-value pair to Hazelcast and `get` operation returns the value together with Container Name. Container Name is used to show that the value is returned from any Pod inside the Kubernetes cluster to prove the true nature of Distributed Cache.

### Quarkus Native Executable

Client-Server code sample can be built and pushed to your own Docker Hub or some other registry via following command but that is optional.
If you decide to build your own image then you should update `hazelcast-quarkus-client.yaml` file with `YOUR-NAME/hazelcast-quarkus-native` as a new image.

```
$ mvn clean package -Pnative -Dnative-image.docker-build=true
$ docker build . -f Dockerfile.native -t YOUR-NAME/hazelcast-quarkus-native
$ docker login
$ docker push YOUR-NAME/hazelcast-quarkus-native
```

Pre-built image used in this guide is located [here](https://hub.docker.com/r/mesut/hazelcast-quarkus-native)

### Launch Hazelcast Cluster
```
docker-compose -f hazelcast-cluster.yml up -d
```

Verify 2 members joined to hazelcast cluster

```
$ docker logs node1 -f
...
 Members {size:2, ver:2} [
	Member [172.30.0.2]:5701 - b123eadd-e3e6-4605-867d-1f1f40a2eaf2 this
	Member [172.30.0.3]:5701 - c31026b6-e01d-45f7-b3b2-3d0338d67ef2
]
...
```

### Launch Quarkus Microservices

Launch Quarkus Microservice containers.
```
docker-compose -f hazelcast-quarkus-client.yml up -d
```

Check logs to see if quarkus started properly.

```
$ docker logs hazelcast-quarkus1 -f
2019-12-13 23:40:27,298 INFO  [io.quarkus] (main) hazelcast-quarkus-guide 1.0-SNAPSHOT (running on Quarkus 1.0.0.Final) started in 0.014s. Listening on: http://0.0.0.0:8080
2019-12-13 23:40:27,299 INFO  [io.quarkus] (main) Profile prod activated. 
2019-12-13 23:40:27,299 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy, resteasy-jackson, resteasy-jsonb]...
```

Send a put operation to the first microservice running on port 8081
```
$ curl "localhost:8081/hazelcast/put?key=key_1&value=value_1";echo;
{"containerName":"hazelcast-quarkus_2","value":"value_1"}
```

Get the value from microservice running on 8080 and verify that it is the same value as put operation
```
$ curl "localhost:8080/hazelcast/get?key=key_1";echo;
{"containerName":"hazelcast-quarkus_1","value":"value_1"}
```

Clean Up
```
$ docker-compose -f hazelcast-quarkus-client.yml down
$ docker-compose -f hazelcast-cluster.yml down
```

## Conclusion 
Hazelcast is fastest cloud native distributed cache solution in OpenSource world. It is elastic and very natural fit for cloud-ready architectures.
Quarkus tailors your application for GraalVM and HotSpot. It has amazingly fast boot time, incredibly low RSS memory (not just heap size!) offering near instant scale up. This guide will help you to quickly start experimenting both technologies in your development environment.
