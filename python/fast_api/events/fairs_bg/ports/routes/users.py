from fastapi import APIRouter, Depends
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.user.model import User
from fairs_bg.business.user.service import UserService

from fairs_bg.ports.db.dao.users import UserAlchemy, get_user_dao
from fairs_bg.ports.routes.services.users import UserCommandService


def get_user_service(dao:UserAlchemy = Depends(get_user_dao)) -> UserCommandService:
    service = UserService(dao)
    return UserCommandService(service)

router = APIRouter(prefix="/users",
                   tags=["users"])

@router.get("/{id}",summary="Retrieve specific user")
async def get_user_by_id(id:int,service:UserCommandService = Depends(get_user_service))-> User:
    return await service.get(id)

@router.post("",summary="Create new user or save existing one")
async def save_user(user:User,service:UserCommandService = Depends(get_user_service)) -> User:
    return await service.save(user)

@router.delete("/{id}",summary="Delete user")
async def delete(id:int,service:UserCommandService = Depends(get_user_service))-> None:
    await service.delete(id)