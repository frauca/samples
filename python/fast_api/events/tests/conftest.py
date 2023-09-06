
from fairs_bg.business.user.model import User
import pytest

@pytest.fixture
def user()-> User:
    return User(id=20,name="fixture_user", email="fixture@mail.com")
