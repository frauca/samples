from fastapi.testclient import TestClient
from sqlalchemy.orm import Session

from fairs_bg.business.user.model import User


def test_get_valid_user(client:TestClient,db:Session, user:User)->None:
    new_user = User(**user.model_dump())
    new_user.id = None
    new_user.name = 'get valid test user'
    response = client.post("/users",json=new_user.model_dump())
    user_id = response.json()['id']
    assert user_id
    response = client.get(f"/users/{user_id}")

    assert response.status_code == 200
    assert response.json()['name'] == "get valid test user"

    client.delete(f"/users/{user_id}")
    
def test_user_not_found(client:TestClient)->None:
    response = client.get(f"/users/666666")
    assert response.status_code == 404
    assert response.json()['title'] == "It seems like we couldn't find what you're looking for."
    assert response.json()['detail'] == "The user with the id '666666' could not be found"
    assert response.json()['status'] == 404
    assert response.json()['instance'] == "/users/666666"
    assert response.json()['name'] == "NOT_FOUND"
    assert response.json()['code'] == "000002"

def test_duplicated_email(client:TestClient, user:User)->None:

    original_user = User(**user.model_dump())
    original_user.id = None
    original_user.email = 'duplicated@email.com'

    duplicated_user = User(**original_user.model_dump())
    duplicated_user.name = f"Not the same as {original_user.name}"

    response = client.post("/users",json=original_user.model_dump())

    assert response.status_code == 200
    assert response.json()['email'] == "duplicated@email.com"

    response = client.post("/users",json=duplicated_user.model_dump())
    assert response.status_code == 400
    assert response.json()['name'] == "USER_DUPLICATED_EMAIL"
    assert response.json()['code'] == "010001"
    assert response.json()['title'] == "Already know this user with another name."
    assert response.json()['detail'] == f"The email is already present in our system. To update the user you need to know the id of the user."

def test_delete_none_existing_user(client:TestClient) -> None:
    response = client.delete(f"/users/666666")
    assert response.status_code == 200

