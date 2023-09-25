from unittest.mock import AsyncMock
import pytest
from fairs_bg.business.events.service import EventDao

from fairs_bg.business.user.model import User
from fairs_bg.business.user.service import UserService, UserDao


@pytest.fixture
def user_dao() -> UserDao:
    dao = AsyncMock(UserDao)
    return dao


@pytest.fixture
def user_service(user_dao: UserDao) -> UserService:
    return UserService(user_dao)


@pytest.fixture
def event_dao() -> EventDao:
    return AsyncMock(EventDao)
