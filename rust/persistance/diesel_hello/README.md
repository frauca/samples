# Diesel demo

I'm following https://diesel.rs/guides/getting-started/

## Creating enviroment

```shell
sudo apt install libpq-dev
cargo install diesel_cli --no-default-features --features postgres
echo DATABASE_URL=postgres://hello:secret@localhost/hello > .env
```