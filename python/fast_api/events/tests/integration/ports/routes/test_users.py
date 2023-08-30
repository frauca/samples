from fastapi.testclient import TestClient
from sqlalchemy.orm import Session

from fairs_bg.business.user.model import User
from fairs_bg.ports.db.dao.users import UserDB


def test_get_valid_user(client:TestClient,db:Session, user:User)->None:
    new_user = UserDB(**user.model_dump())
    new_user.name = 'get valid test user'
    db.add(new_user)
    db.commit()
    response = client.get(f"/users/{new_user.id}")
    assert response.status_code == 200
    assert response.json()['name'] == "get valid test user"
    db.delete(new_user)
    db.commit()