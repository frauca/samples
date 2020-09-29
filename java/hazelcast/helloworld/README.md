#Example with hazelcast

The objective of this project is to make a simple is to make a simple example with a simple web application working with hazelcast client with an external cluster.


* Fist commit will be the simple application working 
* Second commit to make the same application work on a kubernetes
* Adding hazelcast to the solution

## Making maven image
Make the docker image 

```
mvn jib:dockerBuild -Dimage=frauca/hazelcast-simple-sample
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
