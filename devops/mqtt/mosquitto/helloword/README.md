# Mosquitto 

Just basic send recibe subscribe with mosquitto

# run on local

```shell
docker run  -ti --rm --name mosquitto -p 1883:1883 -p 9001:9001 -v mosquitto.conf:/mosquitto/config/mosquitto.conf eclipse-mosquitto
```

opne two terminals and login inside docker running

```shell
docker exec -it mosquitto /bin/sh
mosquitto_sub -d -t hello/world
```


```shell
docker exec -it mosquitto /bin/sh
mosquitto_pub -d -t hello/world -m "Hello from Terminal window 2!"
```

# I have problems with the previous one

```shell
docker run -ti --rm --name mosquitto -p 1883:1883 -p 9001:9001 toke/mosquitto
```