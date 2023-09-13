from typing import Annotated
from fastapi import Depends, FastAPI, HTTPException, status
from fastapi.security import OAuth2AuthorizationCodeBearer


app = FastAPI()
oauth2_scheme = OAuth2AuthorizationCodeBearer(authorizationUrl="/auth-code/callback",tokenUrl="token")

async def get_current_user(token: Annotated[str, Depends(oauth2_scheme)]) -> str:
    user = token
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid authentication credentials",
            headers={"WWW-Authenticate": "Bearer"},
        )
    return user

@app.get("/protected")
def protected(user: Annotated[str, Depends(get_current_user)])->str:
    return f"You need to be logged to see this. I think you are {user}"