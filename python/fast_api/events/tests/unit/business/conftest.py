
from unittest.mock import MagicMock
import pytest

from fairs_bg.business.ports.persistance import Dao
from fairs_bg.business.user.model import User
from fairs_bg.business.user.service import UserService


@pytest.fixture
def user_dao()-> Dao[User]:
    dao = MagicMock(Dao[User])
    return dao

@pytest.fixture
def user_service(user_dao:Dao[User]) -> UserService:
    return UserService(user_dao)