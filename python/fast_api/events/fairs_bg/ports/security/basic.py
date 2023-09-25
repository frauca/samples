from typing import Annotated
from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPBasic, HTTPBasicCredentials
from fairs_bg.business.errors.fairs_error import FairsException

from fairs_bg.business.user.model import User
from fairs_bg.ports.routes.users.service import UserCommandService, get_user_service


security = HTTPBasic()


async def get_authenticated_user(
    credentials: Annotated[HTTPBasicCredentials, Depends(security)],
    service: UserCommandService = Depends(get_user_service),
) -> User:
    try:
        user = await service.findByEmail(credentials.username)
    except FairsException:
        user = None
    if not user or credentials.password != "DoNotTellNoOne":
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect email or password",
            headers={"WWW-Authenticate": "Basic"},
        )
    return user
