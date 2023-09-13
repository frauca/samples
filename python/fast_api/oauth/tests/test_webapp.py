import httpx

from commons.commons import get_client_id_1, get_okta_api, get_okta_authorize, get_okta_headers, get_user_dict, random_state


def test_protected_is_logged()-> None:
    response = httpx.get("http://localhost:8080/protected")

    assert response.status_code == 401

def test_access_protected() -> None:
    

    data = get_user_dict()
    data["stateToken"]="00quAZYqYjXg9DZhS5UzE1wrJuQ6KKb_kzOeH7OGB5"
    response = httpx.post(get_okta_api(),
                          headers=get_okta_headers(),
                          data=data)

    assert response.status_code == 200
    assert response.json()['_links']['ref']['href'] == 'patata'
    