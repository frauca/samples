
from person.person import Person


def test_put_dao(mocked_db, person_service)->None:
    person = Person(id=2, name = "joan", mail="joan@mail.com")
    mocked_db.put.return_value = None

    person_service.save(person)

    mocked_db.put.assert_called_once()