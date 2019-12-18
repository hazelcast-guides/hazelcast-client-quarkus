# Hazelcast and Quarkus

<a href="https://github.com/actions/toolkit"><img alt="GitHub Actions status" src="https://github.com/hazelcast-guides/hazelcast-quarkus/workflows/build/badge.svg"></a>

This guide shows how to setup [Hazelcast](https://github.com/hazelcast/hazelcast) with [Quarkus](https://github.com/quarkusio/quarkus) in a containerized environment. 

## Requirements

### Docker

Make sure that both Docker and Docker Compose are installed. 
```
$ docker -v
Docker version 19.03.5, build 633a0ea
$ docker-compose -v
docker-compose version 1.24.1, build 4667896b
```

If Docker isn't installed, check the official Getting Started guide: [Docker Getting Started](https://www.docker.com/get-started)

## Hazelcast-Quarkus Code Sample

This guide showcases how to setup a basic Quarkus application to work with Hazelcast Client-Server Topology. 

The `put` operation places a key-value pair to Hazelcast and `get` operation returns the value along with the Container Name.
 
The Container Name is present to make it clear from which instance the value is returned from.

### Quarkus Native Executable

To build a native executable, you can use the dedicated _native_ Maven profile:

```
mvn clean package -Pnative -Dnative-image.docker-build=true
```

The `-Dnative-image.docker-build=true` build parameter runs the native compilation inside a special GraalVM-enabled Docker container provided by Quarkus.
However, if you wish, you can use your local GraalVM setup.

Now, we're ready to build the Docker image (based on a dedicated Dockerfile: `Dockerfile.native`):

```
docker build . -f Dockerfile.native -t hazelcast-guides/hazelcast-quarkus-native
```

### Application

We're ready to launch the application consisting of a Hazelcast cluster and two Quarkus applications:

```
docker-compose up -d
```

We can verify that two members joined the Hazelcast cluster:

```
$ docker logs node1 -f
...
 Members {size:2, ver:2} [
	Member [172.30.0.2]:5701 - b123eadd-e3e6-4605-867d-1f1f40a2eaf2 this
	Member [172.30.0.3]:5701 - c31026b6-e01d-45f7-b3b2-3d0338d67ef2
]
...
```

We can verify that Quarkus applications started properly (notice the startup time of 14ms!):

```
$ docker logs hazelcast-quarkus1 -f
2019-12-13 23:40:27,298 INFO  [io.quarkus] (main) hazelcast-quarkus-guide 1.0-SNAPSHOT (running on Quarkus 1.0.0.Final) started in 0.014s. Listening on: http://0.0.0.0:8080
2019-12-13 23:40:27,299 INFO  [io.quarkus] (main) Profile prod activated. 
2019-12-13 23:40:27,299 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy, resteasy-jackson, resteasy-jsonb]...
```

Send a put operation to the first application running on port 8081:
```
$ curl -X POST "localhost:8081/hazelcast/put?key=key_1&value=value_1";echo;
{"containerName":"hazelcast-quarkus_2","value":"value_1"}
```

Get the value from application running on 8080 and verify that it's the same value as put operation:
```
$ curl "localhost:8080/hazelcast/get?key=key_1";echo;
{"containerName":"hazelcast-quarkus_1","value":"value_1"}
```

Clean Up
```
$ docker-compose down
```

## Conclusion 
Hazelcast is fastest cloud native distributed cache solution in OpenSource world. It is elastic and very natural fit for cloud-ready architectures.
Quarkus tailors your application for GraalVM and HotSpot. It has amazingly fast boot time, incredibly low RSS memory (not just heap size!) offering near instant scale up. This guide will help you to quickly start experimenting both technologies in your development environment.
