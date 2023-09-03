from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.user.model import User
from fairs_bg.business.user.service import UserService
from fairs_bg.ports.db.sqlalchemy import transactional


class UserCommandService:
    def __init__(self, service: UserService) -> None:
        self.service = service

    def get(self,id:int)->User:
        user = self.service.get(id)
        if not user:
            raise FairsException(ErrorCode.NOT_FOUND,f"The user with the id '{id}' could not be found")
        return user
    
    def save(self,user:User) -> User:
        def service_save()->User:
            self.service.save(user)
            return user
        return transactional(self.service.dao,service_save)
    
    def delete(self,id:int)->None:
        def service_delete()->None:
            self.service.delete(id)
        return transactional(self.service.dao, service_delete)