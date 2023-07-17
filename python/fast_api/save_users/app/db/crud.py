from typing import List
from sqlalchemy.orm import Session
from app.db.users import UserDB

def all_users(db:Session)->List[UserDB]:
    return db.query(UserDB).all()


def create_user(db:Session, name:str)->UserDB:
    user = UserDB(name = name)
    db.add(user)
    db.commit()
    db.refresh(user)
    return user