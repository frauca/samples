from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

from app.settings import Settings


class Database:

    def __init__(self, settings: Settings):
        engine = create_engine(settings.database_url, connect_args={"check_same_thread": False})
        self.session = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()