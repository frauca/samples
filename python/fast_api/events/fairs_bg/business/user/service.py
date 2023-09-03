from fairs_bg.business.user.model import User,UserDao


class UserService:
    def __init__(self, dao: UserDao) -> None:
        self.dao: UserDao = dao

    def get(self, id:int) -> User | None:
        return self.dao.findById(id)
    
    def save(self, user:User) -> None:
        self.dao.save(user)

    def delete(self, id:int) -> None:
        self.dao.delete(id) 