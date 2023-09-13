import httpx

from commons.api import  get_okta_api, get_okta_headers, get_user_dict,get_redirect_uri


def test_protected_is_logged()-> None:
    response = httpx.get("http://localhost:8080/protected")

    assert response.status_code == 401

def test_print_uri()->None:
    get_redirect_uri()

def test_access_protected() -> None:
    
    login_url = "http://localhost:8080/authorization-code/callback?code=9LlOBWfxfoA4aTofDvMB_GCQP_hVyq7Sc7pY-pXssMY&state=b%277d8466fdc0e9b52c2219e8478faac3%27"

    data = get_user_dict()
    data["stateToken"]="00quAZYqYjXg9DZhS5UzE1wrJuQ6KKb_kzOeH7OGB5"
    response = httpx.post(get_okta_api(),
                          headers=get_okta_headers(),
                          data=data)

    assert response.status_code == 200
    assert response.json()['_links']['ref']['href'] == 'patata'
    