import abc
from collections.abc import Iterable
from typing import Generic, TypeVar
from pydantic import BaseModel


class Persistable(BaseModel):
    id: int

P = TypeVar("P")


class Dao(Generic[P], metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def findById(self, id: int) -> P:
        raise NotImplementedError
