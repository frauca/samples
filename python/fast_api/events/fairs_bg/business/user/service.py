import abc
from fairs_bg.business.ports.persistance import ModelDao
from fairs_bg.business.user.model import User


class UserDao(ModelDao[User], metaclass=abc.ABCMeta):
    @abc.abstractmethod
    async def findByEmail(self, email: str) -> User | None:
        raise NotImplementedError


class UserService:
    def __init__(self, dao: UserDao) -> None:
        self.dao: UserDao = dao

    async def get(self, id: int) -> User | None:
        return await self.dao.findById(id)

    async def findByEmail(self, email: str) -> User | None:
        return await self.dao.findByEmail(email)

    async def save(self, user: User) -> User:
        return await self.dao.save(user)

    async def delete(self, id: int) -> None:
        await self.dao.delete(id)
