from datetime import datetime
from typing import Annotated

from pydantic import Field
from fairs_bg.business.ports.persistance import Persistable
from fairs_bg.business.user.model import User


class Event(Persistable):
    name: Annotated[str, Field(min_length=3, max_length=63)]
    begining: datetime
    ending: datetime
    organizer: User
