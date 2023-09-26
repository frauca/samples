from datetime import datetime
from os import name
from typing import Optional
from fastapi import Depends
from pydantic import BaseModel
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException

from fairs_bg.business.events.model import Event
from fairs_bg.business.events.service import EventDao, EventService
from fairs_bg.business.user.model import User
from fairs_bg.ports.db.dao.events import EventAlchemy, get_event_dao


class EventRest(BaseModel):
    id: int|None = None
    name: str
    begining: datetime
    ending: datetime


class EventRestAdapter:
    @staticmethod
    def to_business(event: EventRest, organizer: User) -> Event:
        if not organizer or not organizer.id:
            raise FairsException(
                ErrorCode.UNEXPECTED,
                f"When creating the event {event.name} we do not know who is making it",
            )
        return Event(
            id=event.id,
            name=event.name,
            begining=event.begining,
            ending=event.ending,
            organizer_id=organizer.id,
        )

    @staticmethod
    def to_rest(event: Event) -> EventRest:
        return EventRest(**event.model_dump())


class EventCommandService:
    def __init__(self, service: EventService) -> None:
        self.service = service

    async def save(self, input_event: EventRest, organizer: User) -> EventRest:
        event = EventRestAdapter.to_business(input_event, organizer)
        saved = await self.service.save(event)
        return EventRestAdapter.to_rest(saved)


def get_event_service(
    event_dao: EventAlchemy = Depends(get_event_dao),
) -> EventCommandService:
    service = EventService(event_dao)
    return EventCommandService(service)
