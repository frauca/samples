from typing import Generator
from fastapi import Request
from sqlalchemy import create_engine
from sqlalchemy.orm import Session, sessionmaker, declarative_base
from fairs_bg.ports.config.settings import FairsSettings



def session(settings:FairsSettings)->Session:
        engine = create_engine(settings.database_url, connect_args={})
        return sessionmaker(bind=engine)()

def get_db(request:Request)->Generator[Session,None,None]:
    settings:FairsSettings = request.app.state.settings
    db = session(settings)
    try:
        yield db
    finally:
        db.close()

Base = declarative_base()