import abc
from typing import Generic, TypeVar
from pydantic import BaseModel

class Persistable(BaseModel):
    id: int | None

P = TypeVar("P",bound=Persistable)

class ModelDao(Generic[P], metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def findById(self, id: int) -> P | None:
        raise NotImplementedError
    
    @abc.abstractmethod
    def save(self,entity:P)->P:
        raise NotImplementedError
    
    @abc.abstractmethod
    def delete(self,id:int)->None:
        raise NotImplementedError
    