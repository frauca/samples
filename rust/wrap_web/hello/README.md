#Learnign warp

I have to learn rust for the work. This is a sample to make it with warp. I'm used to spring, this low level stuff is hard for me.


## Test it for myself


```shell
curl -X POST http://localhost:3030/items \
--header 'Content-Type: application/json' \
-d '{"name":"sample","quantity":1}'

curl http://localhost:3030/items \
--header 'Content-Type: application/json'
```