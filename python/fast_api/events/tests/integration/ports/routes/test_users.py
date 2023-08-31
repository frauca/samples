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

def test_user_not_found(client:TestClient)->None:
    response = client.get(f"/users/666666")
    assert response.status_code == 404
    assert response.json()['title'] == "It seems like we couldn't find what you're looking for."
    assert response.json()['detail'] == "The user with the id '666666' could not be found"
    assert response.json()['status'] == 404
    assert response.json()['instance'] == "/users/666666"
    assert response.json()['name'] == "NOT_FOUND"
    assert response.json()['code'] == "000002"