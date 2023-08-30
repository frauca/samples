from fastapi import Depends
from sqlalchemy import Column, Integer, String

from fairs_bg.business.ports.persistance import Dao
from fairs_bg.business.user.model import User
from sqlalchemy.orm import Session

from fairs_bg.ports.db.sqlalchemy import Base, get_db

class UserDB(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, index=True)    
    email = Column(String, unique=True, index=True)

class UserDao(Dao[User]):
    def __init__(self, db: Session) -> None:
        self.db = db

    def findById(self, id: int) -> User | None:
        user_db:UserDB | None = self.db.get(UserDB,id)
        if not user_db:
            return None
        return self._adapt_from_db(user_db)
    
    def _adapt_from_db(self,user:UserDB) -> User:
        return User(**user.__dict__)
    
    def _adapt_from_business(self,user:User) -> UserDB:
        return UserDB(**user.model_dump())

def get_user_dao(db:Session = Depends(get_db)) -> Dao[User]:
    return UserDao(db)
