from typing import Coroutine
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.user.model import User
from fairs_bg.business.user.service import UserService
from fairs_bg.ports.db.sqlalchemy import transactional


class UserCommandService:
    def __init__(self, service: UserService) -> None:
        self.service = service

    async def get(self,id:int)->User:
        user =  await self.service.get(id)
        if not user:
            raise FairsException(ErrorCode.NOT_FOUND,f"The user with the id '{id}' could not be found")
        return user
    
    async def save(self,user:User) -> User:
        async def service_save()->User:
            return await self.service.save(user)
        return await transactional(self.service.dao,service_save())
    
    async def delete(self,id:int)->None:
        async def service_delete()->None:
            await self.service.delete(id)
        return await transactional(self.service.dao, service_delete())