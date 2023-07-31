from mock import Mock, patch
from db.database import DB, BaseDao
from db.entity import BaseEntity

def test_simple_crud(empty_db:DB) -> None:
    assert empty_db.findById("1") == None
    assert empty_db.put("1","initial value") == None
    assert empty_db.findById("1") == "initial value"
    assert empty_db.put("1","next value") == "initial value"
    assert empty_db.delete("1") == "next value"
    assert empty_db.findById("1") == None

@patch("db.database.DB")
def test_get_dao(patched_db:DB):
    dao = BaseDao(patched_db,"entity_name")
    patched_db.findById.return_value = f'{{"id":1}}'

    entity = dao.findById(1)

    assert entity.id == 1
    patched_db.findById.assert_called_once_with('entity_name-1')

def test_put_dao(mocked_db, base_dao)->None:
    entity = BaseEntity(id=2)
    mocked_db.put.return_value = None

    base_dao.put(entity)

    mocked_db.put.assert_called_once()
    
