from fairs_bg.business.user.model import User,UserDao


class UserService:
    def __init__(self, dao: UserDao) -> None:
        self.dao: UserDao = dao

    async def get(self, id:int) -> User | None:
        return await self.dao.findById(id)
    
    async def save(self, user:User) -> User:
        return await self.dao.save(user)

    async def delete(self, id:int) -> None:
        await self.dao.delete(id) 