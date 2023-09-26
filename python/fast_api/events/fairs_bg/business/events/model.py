from datetime import datetime
from typing import Annotated, Self

from pydantic import Field,  model_validator
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.ports.persistance import Persistable
from fairs_bg.business.user.model import User


class Event(Persistable):
    name: Annotated[str, Field(min_length=3, max_length=63)]
    begining: datetime
    ending: datetime
    organizer_id: int

    @model_validator(mode='after')
    def validate_dates(self) -> Self:
        if self.begining > self.ending:
            raise FairsException(
                ErrorCode.EVENT_DATE_ENDS_BEFORE_START,
                f"End date {self.ending} must be after start date {self.begining}",
            )
        return self
