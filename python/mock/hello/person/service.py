from db.person import PersonDao
from person.person import Person


class PersonService:

    def __init__(self,dao:PersonDao) -> None:
        self.dao = dao

    
    def save(self, person:Person) -> Person | None:
        return self.dao.put(person)
    
    def get(self, id:int) -> Person | None:
        return self.dao.findById(id)