from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base, Session

from app.settings import Settings


def session(settings:Settings)->Session:
        engine = create_engine(settings.database_url, connect_args={"check_same_thread": False})
        return sessionmaker(autocommit=False, autoflush=False, bind=engine)()

Base = declarative_base()