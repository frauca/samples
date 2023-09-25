from fastapi import APIRouter
from fairs_bg.ports.routes.users.routes import router as user_routes
from fairs_bg.ports.routes.events.routers import router as events_routes


endpoints = APIRouter()

endpoints.include_router(user_routes)
endpoints.include_router(events_routes)