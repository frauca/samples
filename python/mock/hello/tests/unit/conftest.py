from mock import Mock
import pytest
from db.person import PersonDao
from db.database import DB
from person.service import PersonService

@pytest.fixture()
def empty_db()->DB:
    return DB()

@pytest.fixture()
def mocked_db() -> DB:
    return Mock(spec=DB)

@pytest.fixture()
def person_dao(mocked_db:DB) -> PersonDao:
    return PersonDao(mocked_db)

@pytest.fixture()
def person_service(person_dao) -> PersonService:
    return PersonService(person_dao)