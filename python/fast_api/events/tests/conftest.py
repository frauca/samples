from datetime import datetime, timedelta
from fairs_bg.business.events.model import Event
from fairs_bg.business.user.model import User
import pytest

from tests import commons


@pytest.fixture(scope="session")
def user() -> User:
    return User(id=1, name="first user", email="first@fake.mail")


@pytest.fixture
def unsaved_user(user: User) -> User:
    cloned_user = User(**user.model_dump())
    cloned_user.id = None
    cloned_user.name = "user_" + commons.random_string(8)
    cloned_user.email = cloned_user.name + "@fake.mail"
    return cloned_user


@pytest.fixture
def event(user: User) -> Event:
    return Event(
        id=1,
        name="first user event",
        begining=datetime.now(),
        ending=datetime.now() + timedelta(days=5),
        organizer=user,
    )


@pytest.fixture
def unsaved_event(event: Event, unsaved_user: User) -> Event:
    cloned_event = Event(**event.model_dump())
    cloned_event.id = None
    cloned_event.organizer = unsaved_user
    return cloned_event
