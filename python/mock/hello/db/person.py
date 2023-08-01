from abc import abstractmethod
from db.database import DB, BaseDao
from person.person import Person


class PersonDao(BaseDao[Person]):
    
    def __init__(self, db: DB) -> None:
        super().__init__(db, "person")
    
    def _from_json(self, json:str)->Person:
        return Person.model_validate_json(json)

    