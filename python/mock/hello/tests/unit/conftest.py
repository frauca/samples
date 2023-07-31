from mock import Mock
import pytest
from db.database import DB, BaseDao

@pytest.fixture()
def empty_db()->DB:
    return DB()

@pytest.fixture()
def mocked_db() -> DB:
    return Mock(spec=DB)

@pytest.fixture()
def base_dao(mocked_db:DB) -> BaseDao:
    return BaseDao(mocked_db,"base_dao")