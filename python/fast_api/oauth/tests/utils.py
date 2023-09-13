from commons.commons import get_client_id_1, get_okta_authorize, random_state


def get_redirect_uri()-> None:
    client_id = get_client_id_1()
    state = random_state()
    redirect_uri = get_okta_authorize(client_id,8080,state)
    print(f"open {redirect_uri}")

get_redirect_uri()