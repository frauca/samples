from typing import Generator
from dotenv import dotenv_values
from fastapi import Depends, FastAPI
from app.db import database, crud
from app.db.users import UserDB
from app.models.users import User
from app.settings import Settings
from sqlalchemy.orm import Session

config = Settings()
app = FastAPI()

def get_db()->Generator[Session,None,None]:
    db = database.session(config)
    try:
        yield db
    finally:
        db.close()


@app.get("/")
async def root():
    return {"message": "SimpleExample"}


@app.get("/users",response_model=list[User])
async def all_users(db:Session = Depends(get_db))->list[UserDB]:
    return crud.all_users(db)


@app.get("/users/add",response_model=User)
async def create_user(name:str, db:Session = Depends(get_db))->UserDB:
    return crud.create_user(db, name)