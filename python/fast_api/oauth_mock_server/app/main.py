from typing import Annotated
from fastapi import Depends, FastAPI, HTTPException, status
from fastapi.security import OAuth2PasswordBearer

from jwt import PyJWKClient
import jwt

app = FastAPI()
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

async def get_current_user(token: Annotated[str, Depends(oauth2_scheme)]) -> str:
    try:
        if not token:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid authentication credentials",
                headers={"WWW-Authenticate": "Bearer"},
            )
        jwks_client = PyJWKClient("http://localhost:5000/oauth/jwks")
        signing_key = jwks_client.get_signing_key_from_jwt(token)
        claims = jwt.decode(token,
                                        signing_key.key,
                                        algorithms=["ES256"],
                                        audience=['aitrios-gcs'],
                                        issuer='sssauth',
                                        require=["sub", "iss", "aud", "project"]
                                        )
        if not "sub" in claims:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid authentication token",
                headers={"WWW-Authenticate": "Bearer"},
            )
        return claims["sub"]
    except Exception as error:
        print(f"some error {error}")
        raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail=f"Invalid authentication token: {str(error)}",
                headers={"WWW-Authenticate": "Bearer"},
            )

@app.get("/hello")
def read_root(current_user: Annotated[str, Depends(get_current_user)]) -> dict[str, str]:
    return {"msg":f"Hello {current_user}"}