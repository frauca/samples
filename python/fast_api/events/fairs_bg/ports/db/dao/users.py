from typing import Tuple
from fastapi import Depends
from sqlalchemy import Column, Integer, Select, String
from fairs_bg.business.user.model import User, UserDao
from sqlalchemy.orm import Session
from sqlalchemy import select

from fairs_bg.ports.db.sqlalchemy import Base, BaseAlchemyDao, get_db

class UserDB(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, index=True)    
    email = Column(String, unique=True, index=True)

class UserAlchemy(UserDao,BaseAlchemyDao[User,UserDB]):
    def __init__(self, db: Session) -> None:
        super().__init__(db,UserDB)
    
    def findByName(self, name: str) -> User | None:
        stmt:Select[Tuple[UserDB]] = select(UserDB).where(UserDB.name == name)
        return self.db.scalar(stmt)
    
    def _adapt_from_db(self,user:UserDB) -> User:
        return User(**user.__dict__)
    
    def _adapt_from_business(self,user:User) -> UserDB:
        return UserDB(**user.model_dump())
    

def get_user_dao(db:Session = Depends(get_db)) -> UserAlchemy:
    return UserAlchemy(db)
