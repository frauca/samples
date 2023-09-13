import binascii
import os

def get_okta_headers()->dict:
    return {
        "Accept": "application/json",
        "Content-Type": "application/json"
    }

def get_user_dict()->dict:
    return {
        "username": os.environ["OKTA_DOMAIN"],
        "password": os.environ["OKTA_PASS"]
    }

def get_okta_api()->str:
    return f"{get_okta_base()}/api/v1/authn"

def get_okta_base()->str:
    return f'https://{os.environ["OKTA_DOMAIN"]}'

def get_client_id_1()->str:
    return os.environ["OKTA_WEB1_CLIENT_ID"]

def random_state()->str:
    return binascii.b2a_hex(os.urandom(15))

def get_okta_authorize(client_id:str,port:int,state:str)->str:
    return f"{get_okta_base()}/oauth2/v1/authorize?client_id={client_id}&response_type=code&scope=openid&redirect_uri=http%3A%2F%2Flocalhost%3A{port}%2Fauthorization-code%2Fcallback&state={state}"

def get_redirect_uri()-> None:
    client_id = get_client_id_1()
    state = random_state()
    redirect_uri = get_okta_authorize(client_id,8080,state)
    print(f"open {redirect_uri}")