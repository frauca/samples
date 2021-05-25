This is simple example to load configurations

We will have a configuration in path and another on jar. That config will place on a file. That file could be on classpath or on file source

The program will load that file and print it's output

#Run

To run this program compile it first

```shell
./mvnw clean package
java -jar ./target/*.jar
```

Then if you want to get output application properties

```shell
cd assets
java -cp . -jar ../target/*.jar
```

injecting the property

```shell
java  -jar ./target/*.jar --file=file:$(pwd)/assets/sample.txt
```

#References 

* https://docs.spring.io/spring-boot/docs/1.0.1.RELEASE/reference/html/boot-features-external-config.html