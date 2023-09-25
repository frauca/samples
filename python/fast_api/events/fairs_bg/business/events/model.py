from datetime import datetime
from typing import Annotated

from pydantic import Field, validator
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.ports.persistance import Persistable
from fairs_bg.business.user.model import User


class Event(Persistable):
    name: Annotated[str, Field(min_length=3, max_length=63)]
    begining: datetime
    ending: datetime
    organizer_id: int

    @validator("ending", pre=True, always=True)
    @classmethod
    def validate_dates(cls, ending: datetime, values) -> datetime:
        begining: datetime = values.get("begining")
        if begining > ending:
            raise FairsException(
                ErrorCode.EVENT_DATE_ENDS_BEFORE_START,
                f"End date {ending} must be after start date {begining}",
            )
        return ending
