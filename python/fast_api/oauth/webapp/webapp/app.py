import asyncio
from typing import Annotated
from fastapi import Depends, FastAPI, Request
from fastapi.security import OAuth2AuthorizationCodeBearer

from webapp.security import login_code, validate_user

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