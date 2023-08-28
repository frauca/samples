from fastapi import APIRouter
from fairs_bg.ports.routes import users


endpoints = APIRouter()

endpoints.include_router(users.router)