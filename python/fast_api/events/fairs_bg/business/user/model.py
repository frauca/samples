from typing import Annotated
import abc
from pydantic import EmailStr, Field
from fairs_bg.business.ports.persistance import ModelDao, Persistable


class User(Persistable):
    name: Annotated[str,Field(min_length=3, max_length=63)]
    email: EmailStr

class UserDao(ModelDao[User], metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def findByName(self, name:str) -> User|None:
        raise NotImplementedError