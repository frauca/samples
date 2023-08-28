from fairs_bg.business.ports.persistance import Dao
from fairs_bg.business.user.model import User


class UserService:
    def __init__(self, dao: Dao[User]) -> None:
        self.dao: Dao[User] = dao

    def get(self, id:int) -> User:
        return self.dao.findById(id)