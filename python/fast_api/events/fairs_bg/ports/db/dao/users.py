from typing import Tuple
from fastapi import Depends
from psycopg2.errors import UniqueViolation
from sqlalchemy import Column, Integer, Select, String
from sqlalchemy.exc import IntegrityError
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
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
    
    def conver_error(self, error: Exception) -> FairsException:
        if (isinstance(error,IntegrityError) 
            and isinstance(error.orig,UniqueViolation) 
            and "unique_email" in str(error.orig)):
            return FairsException(ErrorCode.USER_DUPLICATED_EMAIL,"The email is already present in our system. To update the user you need to know the id of the user.")
        return super().conver_error(error)
    
    def _adapt_from_db(self,user:UserDB) -> User:
        return User(**user.__dict__)
    
    def _adapt_from_business(self,user:User) -> UserDB:
        return UserDB(**user.model_dump())
    

def get_user_dao(db:Session = Depends(get_db)) -> UserAlchemy:
    return UserAlchemy(db)
