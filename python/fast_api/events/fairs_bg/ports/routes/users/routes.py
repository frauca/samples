from fastapi import APIRouter, Depends
from fairs_bg.business.user.model import User

from fairs_bg.ports.routes.users.service import UserCommandService, get_user_service
from fairs_bg.ports.security.basic import get_authenticated_user


router = APIRouter(
    prefix="/users", tags=["users"], dependencies=[Depends(get_authenticated_user)]
)


@router.get("/{id}", summary="Retrieve specific user")
async def get_user_by_id(
    id: int, service: UserCommandService = Depends(get_user_service)
) -> User:
    return await service.get(id)


@router.post("", summary="Create new user or save existing one")
async def save_user(
    user: User, service: UserCommandService = Depends(get_user_service)
) -> User:
    return await service.save(user)


@router.delete("/{id}", summary="Delete user")
async def delete(
    id: int, service: UserCommandService = Depends(get_user_service)
) -> None:
    await service.delete(id)
