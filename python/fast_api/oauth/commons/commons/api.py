import os
import random
import string

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

def get_client_secret_1()->str:
    return os.environ["OKTA_WEB1_CLIENT_SECRET"]

def random_state()->str:
    letters = string.ascii_lowercase + string.ascii_uppercase + string.digits
    return ''.join(random.choice(letters) for i in range(15))
def get_okta_authorize(client_id:str,port:int,state:str)->str:
    return f"{get_okta_base()}/oauth2/default/v1/authorize?client_id={client_id}&response_type=code&scope=openid%20profile&redirect_uri=http%3A%2F%2Flocalhost%3A{port}%2Fauthorization-code%2Fcallback&state={state}"

def get_okta_token()->str:
    return f"{get_okta_base()}/oauth2/default/v1/token"

def get_okta_authorize_sample()-> str:
    client_id = get_client_id_1()
    state = random_state()
    return get_okta_authorize(client_id,8080,state)

def get_local_callback(port:int)->str:
    return f"http://localhost:{port}/authorization-code/callback"

def get_okta_issuer()->str:
    return f"{get_okta_base()}/oauth2/default"

def set_port(port:int)->None:
    os.environ["APP_PORT"] = str(port)

def get_port()->int:
    return int(os.environ["APP_PORT"])

def get_user_info()->str:
    return f"{get_okta_base()}/oauth2/default/v1/userinfo"