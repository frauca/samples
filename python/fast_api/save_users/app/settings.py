from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    database_url: str = "sqlite:///./local-dev.db"

    class Config:
        env_file = "config.env"
        env_prefix = ""