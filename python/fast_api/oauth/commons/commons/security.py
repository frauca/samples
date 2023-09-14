from fastapi import HTTPException, status
import httpx
from okta_jwt_verifier import AccessTokenVerifier, IDTokenVerifier,JWTUtils
from commons.api import get_client_id_1, get_client_secret_1, get_local_callback, get_okta_issuer, get_okta_token, get_port

import pickle


SESSIONS_FILE="./tokens.session"

def _get_session()->dict[str,str]:
    with open(SESSIONS_FILE, 'rb') as reader:
        return pickle.load(reader)
def load_from_session(id_token:str)->str:
    sessions = _get_session()
    return sessions[id_token]

def save_in_session(id_token:str,access_token:str)->None:
    sessions = _get_session()
    sessions[id_token]=access_token
    with open(SESSIONS_FILE, 'wb') as writer:
        pickle.dump(sessions, writer)

def _get_user_from_token(token:str, claim_name='sub_id')->str:
    headers,claims,signing,signature = JWTUtils.parse_token(token)
    if not claim_name in claims:
         raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail=f"Can not find {claim_name} in claims",
            headers={"WWW-Authenticate": "Bearer"},
        )
    return claims[claim_name]

async def _raise_if_token_id_not_valid(token:str)->None:
    verifier = IDTokenVerifier(get_okta_issuer(),get_client_id_1())
    try:
        await verifier.verify(token)
    except Exception as err:
        raise HTTPException(status.HTTP_401_UNAUTHORIZED, detail=str(err)) from err
    
async def _raise_if_access_token_not_valid(token:str)->None:
    verifier = AccessTokenVerifier(get_okta_issuer())
    try:
        await verifier.verify(token)
    except Exception as err:
        raise HTTPException(status.HTTP_401_UNAUTHORIZED, detail=str(err)) from err

async def validate_user(token:str|None)->str:
    if not token:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid authentication credentials",
            headers={"WWW-Authenticate": "Bearer"},
        )
    await _raise_if_token_id_not_valid(token)
    return _get_user_from_token(token)

async def validate_user_with_access_token(token:str|None)->str:
    if not token:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid authentication credentials",
            headers={"WWW-Authenticate": "Bearer"},
        )
    await _raise_if_access_token_not_valid(token)
    return _get_user_from_token(token,"sub")

async def login_code(code:str|None)->dict:
    port:int = get_port()
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    if not code:
        raise HTTPException(status.HTTP_401_UNAUTHORIZED, "The code was not returned or is not accessible")
    query_params = {'grant_type': 'authorization_code',
                    'code': code,
                    'redirect_uri': get_local_callback(port)
                    }
    with httpx.Client() as client:
        exchange = client.post(
            get_okta_token(),
            headers=headers,
            data=query_params,
            auth=(get_client_id_1(), get_client_secret_1()),
        )
        json_exchange = exchange.json()
    if not json_exchange.get("token_type"):
        raise HTTPException(status.HTTP_401_UNAUTHORIZED, "Unsupported token type. Should be 'Bearer'.")
    access_token = json_exchange.get("access_token")
    id_token = json_exchange.get("id_token")
    save_in_session(id_token,access_token)
    await _raise_if_access_token_not_valid(access_token)
    return json_exchange
