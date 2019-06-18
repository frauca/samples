#OpenFire

## Installation 

I will try to install OpenFire with docker

```text
mkdir /var/lib/openfire

docker run --name openfire -d --restart=always \
  --publish 9090:9090 --publish 5222:5222 --publish 7777:7777 \
  --volume /srv/docker/openfire:/var/lib/openfire \
  gizmotronic/openfire:4.3.2
```

go to http://localhost:9090/ to finish the installation

login with admin and the password, althought user is admin@example.com

## Administration

### logs

```
docker exec -it openfire tail -f /var/log/openfire/info.log
```