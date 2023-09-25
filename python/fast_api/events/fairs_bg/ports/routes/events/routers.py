from fastapi import APIRouter, Depends

from fairs_bg.ports.security.basic import get_authenticated_user


router = APIRouter(
    prefix="/events", tags=["events"], dependencies=[Depends(get_authenticated_user)]
)
