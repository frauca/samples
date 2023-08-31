from fastapi import APIRouter, Depends
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.user.model import User
from fairs_bg.business.user.service import UserService

from fairs_bg.ports.db.dao.users import UserDao, get_user_dao


def get_user_service(dao:UserDao = Depends(get_user_dao)) -> UserService:
    return UserService(dao)

router = APIRouter(prefix="/users",
                   tags=["users"])

@router.get("/{id}",summary="Retrieve specific user")
def get_user_by_id(id:int,user_service:UserService = Depends(get_user_service))-> User | None:
    user = user_service.get(id)
    if not user:
        raise FairsException(ErrorCode.NOT_FOUND,f"The user with the id '{id}' could not be found")
    return user
