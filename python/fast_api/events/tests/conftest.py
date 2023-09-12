from fairs_bg.business.user.model import User
import pytest


@pytest.fixture(scope="session")
def user() -> User:
    return User(id=20, name="first user", email="first@fake.mail")
