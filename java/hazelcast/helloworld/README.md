#Example with hazelcast

The objective of this project is to make a simple is to make a simple example with a simple web application working with hazelcast client with an external cluster.


* Fist commit will be the simple application working 
* Second commit to make the same application work on a kubernetes
* Adding hazelcast to the solution

## Working in kubernetes
Make the docker image 

```
mvn jib:dockerBuild -Dimage=frauca/hazelcast-simple-sample
docker push frauca/hazelcast-simple-sample
```

Then I start minikube

```
minikube start
kubectl apply -f deployment.yaml
```

If you test it

```
kubectl run -i --rm --restart=Never  curl-client --image=tutum/curl:alpine --command -- curl -s 'http://hazelcast-frauca:8080/put?key=roger&value=sample'
kubectl run -i --rm --restart=Never  curl-client --image=tutum/curl:alpine --command -- curl -s 'http://hazelcast-frauca:8080/get?key=roger'
```

You will need to perform a lot times the get to get the correct result as it depends on wich one you are requesting

## Working with hazelcast

Make the docker image 

```
mvn jib:dockerBuild -Dimage=frauca/hazelcast-simple-sample
docker push frauca/hazelcast-simple-sample
```

Then I start minikube

```
minikube delete
minikube start
kubectl apply -f deployment.yaml
```

To check hazelcast cluster

```
kubectl run -i --rm --restart=Never  curl-client --image=tutum/curl:alpine --command -- curl -s 'http://hazelcast-frauca.default.svc.cluster.local:8080/info'
kubectl run -i --rm --restart=Never  curl-client --image=tutum/curl:alpine --command -- curl -s 'http://hazelcast-frauca.default.svc.cluster.local:8080/put?key=roger&value=test'
kubectl run -i --rm --restart=Never  curl-client --image=tutum/curl:alpine --command -- curl -s 'http://hazelcast-frauca.default.svc.cluster.local:8080/get?key=roger'
```

Then get the token and the certificate and replace on hazelcast-client.yaml

```
kubectl get secret hazelcast-service-account-token-6s94h -o jsonpath='{.data.token}' | base64 --decode | xargs echo
kubectl get secret hazelcast-service-account-token-6s94h -o jsonpath='{.data.ca\.crt}' | base64 --decode
```

## Just hazelcast without the client

https://hazelcast.com/blog/configuring-hazelcast-in-non-orchestrated-docker-environments/
https://github.com/pires/hazelcast-kubernetes
https://github.com/hazelcast/hazelcast-code-samples/tree/master/hazelcast-integration/kubernetes/samples/springboot-k8s-hello-world

I made the hazelcast-alone.yaml

```
kubectl apply -f hazelcast-alone.yaml
kubectl run -i --rm --restart=Never  curl-client --image=tutum/curl:alpine --command -- curl -s 'http://service-hazelcast-server.default.svc.cluster.local:5701/hazelcast/rest/maps/mapName/foo'
```
