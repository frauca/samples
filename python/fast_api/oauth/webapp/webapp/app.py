import asyncio
import resource
from typing import Annotated
from fastapi import Depends, FastAPI, HTTPException, Request,status
from fastapi.security import OAuth2AuthorizationCodeBearer
import httpx

from commons.security import load_from_session, login_code, validate_user

app = FastAPI()
oauth2_scheme = OAuth2AuthorizationCodeBearer(authorizationUrl="/auth-code/callback",tokenUrl="token")
loop = asyncio.get_event_loop()

async def get_current_user(token: Annotated[str, Depends(oauth2_scheme)]) -> str:
    return await validate_user(token)

@app.get("/protected")
def protected(user: Annotated[str, Depends(get_current_user)])->str:
    return f"You need to be logged to see this. I think you are {user}"

@app.get("/authorization-code/callback")
async def login(request: Request):
    code = request.query_params.get("code")
    return await login_code(code)

@app.get("/call-resource")
def call_resource(user: Annotated[str, Depends(get_current_user)],
                  token: Annotated[str, Depends(oauth2_scheme)]) -> str:
    access_token = load_from_session(token)
    response = httpx.get("http://localhost:8001/protected",
                         headers={'Authorization': f'Bearer {access_token}'})
    if response.status_code>299:
        raise HTTPException(status.HTTP_500_INTERNAL_SERVER_ERROR, detail=response.text)
    return response.text