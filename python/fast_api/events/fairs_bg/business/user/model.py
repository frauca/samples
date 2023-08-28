from typing import Annotated
from pydantic import EmailStr, Field
from fairs_bg.business.ports.persistance import Persistable


class User(Persistable):
    name: Annotated[str,Field(min_length=3, max_length=63)]
    email: EmailStr
