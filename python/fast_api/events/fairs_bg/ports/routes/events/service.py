from datetime import datetime
from os import name
from pydantic import BaseModel
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException

from fairs_bg.business.events.model import Event
from fairs_bg.business.user.model import User


class EventRest(BaseModel):
    id: int
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


class EventCommandService:
    pass
