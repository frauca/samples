import asyncio
from typing import Annotated
from fastapi import Depends, FastAPI, Request
from fastapi.security import OAuth2AuthorizationCodeBearer

from commons.security import  validate_user_with_access_token

app = FastAPI()
oauth2_scheme = OAuth2AuthorizationCodeBearer(authorizationUrl="/auth-code/callback",tokenUrl="token")
loop = asyncio.get_event_loop()

async def get_current_user(token: Annotated[str, Depends(oauth2_scheme)]) -> str:
    return await validate_user_with_access_token(token)

@app.get("/protected")
def protected(user: Annotated[str, Depends(get_current_user)])->str:
    return f"You need to be logged to see this. I think you are {user}"
