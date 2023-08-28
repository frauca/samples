
from fairs_bg.business.ports.persistance import Dao
from fairs_bg.business.user.model import User
from fairs_bg.business.user.service import UserService


def test_get_user(user_dao: Dao[User], user_service: UserService, user: User)-> None:
    user_dao.findById.return_value = user
    fake_id = 1

    user_found = user_service.get(fake_id)

    assert user_found
    assert user_found.name == user.name
    user_dao.findById.assert_called_once_with(fake_id)