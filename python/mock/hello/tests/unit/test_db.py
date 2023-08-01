from mock import patch
from db.person import PersonDao
from db.database import DB
from person.person import Person

def test_simple_crud(empty_db:DB) -> None:
    assert empty_db.findById("1") == None
    assert empty_db.put("1","initial value") == None
    assert empty_db.findById("1") == "initial value"
    assert empty_db.put("1","next value") == "initial value"
    assert empty_db.delete("1") == "next value"
    assert empty_db.findById("1") == None

@patch("db.database.DB")
def test_get_dao(patched_db:DB):
    dao = PersonDao(patched_db)
    patched_db.findById.return_value = f'{{"id":1,"name":"roger","mail":"my@mail.com"}}'

    entity = dao.findById(1)

    assert entity.id == 1
    assert entity.mail == "my@mail.com"
    assert type(entity) == Person
    patched_db.findById.assert_called_once_with('person-1')

def test_put_dao(mocked_db, person_dao)->None:
    entity = Person(id=2, name = "pep", mail="other@mail.com")
    mocked_db.put.return_value = None

    person_dao.put(entity)

    mocked_db.put.assert_called_once()
    
