from fastapi import APIRouter, Depends
from fairs_bg.business.user.model import User
from fairs_bg.ports.routes.events.service import EventCommandService, EventRest, get_event_service

from fairs_bg.ports.security.basic import get_authenticated_user


router = APIRouter(
    prefix="/events", tags=["events"], dependencies=[Depends(get_authenticated_user)]
)

@router.post("", summary="Create new event")
async def save_event(
    event: EventRest, 
    service: EventCommandService = Depends(get_event_service),
    logged_user:User = Depends(get_authenticated_user)
) -> EventRest:
    return await service.save(event,organizer=logged_user)