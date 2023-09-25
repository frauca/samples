from datetime import datetime, timedelta
from os import name
from typing import Any
from unittest.mock import AsyncMock
from fairs_bg.business.events.model import Event
from fairs_bg.business.user.model import User
import pytest

from tests import commons


@pytest.fixture(scope="session")
def user() -> User:
    return User(id=1, name="first user", email="first@fake.mail")


@pytest.fixture
def event(user: User) -> Event:
    return unsaved_event(user)


def unsaved_user() -> User:
    name = "user_" + commons.random_string(8)
    return User(id=None, name=name, email=name + "@fake.mail")


def unsaved_event(owner: User) -> Event:
    if not owner or not owner.id:
        raise AssertionError("invalid user fixture, could not be none")
    return Event(
        id=None,
        name=f"{owner.name} event {commons.random_string(8)}",
        begining=datetime.now(),
        ending=datetime.now() + timedelta(days=5),
        organizer_id=owner.id,
    )


def new_event() -> Event:
    user: User = unsaved_user()
    user.id = 2
    return unsaved_event(user)


def async_return_value(value: Any) -> AsyncMock:
    mock = AsyncMock()
    mock.return_value = value
    return mock
