import abc
from typing import Callable, Generator, Generic, TypeVar
from fastapi import Request
from sqlalchemy import create_engine
from sqlalchemy.orm import Session, sessionmaker, declarative_base
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.ports.persistance import P, ModelDao
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

PDB = TypeVar('PDB')
T = TypeVar('T')

class BaseAlchemyDao(ModelDao[P],Generic[P,PDB]):
    def __init__(self,db:Session,type:PDB) -> None:
          super().__init__()
          self.db:Session = db
          self.type:PDB = type

    def findById(self, id: int) -> P | None:
         el:PDB|None = self._findByIdDB(id)
         if not el:
              return None
         return self._adapt_from_db(el)
    
    def save(self, model:P)->None:
         entity = self._adapt_from_business(model)
         self.db.add(entity)

    def delete(self, id:int) -> None:
         entity:PDB|None = self._findByIdDB(id)
         if entity:
            self.db.delete(entity)
    
    def _findByIdDB(self, id:int) -> PDB | None:
         return self.db.query(self.type).get(id)
    
    @abc.abstractmethod
    def _adapt_from_db(self,user:PDB) -> P:
        raise NotImplementedError
    
    @abc.abstractmethod
    def _adapt_from_business(self,user:P) -> PDB:
        raise NotADirectoryError
    
def transactional(dao:ModelDao,process: Callable[[],T])->T:
    if not isinstance(dao,BaseAlchemyDao):
         raise FairsException(ErrorCode.UNEXPECTED,"Breaking news: Our code had a mini rebellion. An inspirated developer took a decision that another one did not see it comming. ")
    try:
        result = process()
        dao.db.commit()
        return result
    except Exception as error:
        dao.db.rollback()
        raise error