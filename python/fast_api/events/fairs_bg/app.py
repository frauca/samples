import logging
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.ports.config.settings import FairsSettings
from fairs_bg.logs.setup import get_logger
from fairs_bg.ports import routes
import uvicorn
from fastapi import FastAPI
from sqlalchemy.exc import SQLAlchemyError

from fairs_bg.ports.routes.errors import http_error_handler

SETTINGS_PATH: str = "config.yaml"


def _banner() -> None:
    get_logger(__name__).info(
        """
    8888888b.          888             d8b 888      8888888888       d8b                  
    888   Y88b         888             Y8P 888      888              Y8P                  
    888    888         888                 888      888                                   
    888   d88P .d88b.  888888  8888b.  888 888      8888888  8888b.  888 888d888 .d8888b  
    8888888P" d8P  Y8b 888        "88b 888 888      888         "88b 888 888P"   88K      
    888 T88b  88888888 888    .d888888 888 888      888     .d888888 888 888     "Y8888b. 
    888  T88b Y8b.     Y88b.  888  888 888 888      888     888  888 888 888          X88 
    888   T88b "Y8888   "Y888 "Y888888 888 888      888     "Y888888 888 888      88888P' """
    )

def app_from(settings:FairsSettings) -> FastAPI:
    app = FastAPI()
    app.state.settings = settings
    app.include_router(routes.endpoints)
    app.add_exception_handler(FairsException,http_error_handler)
    app.add_exception_handler(SQLAlchemyError,http_error_handler)
    return app

def generate_app() -> FastAPI:
    settings: FairsSettings = FairsSettings.from_yaml(SETTINGS_PATH)
    return app_from(settings)

def run_with_uvicorn() -> None:
    settings: FairsSettings = FairsSettings.from_yaml(SETTINGS_PATH)
    logging.config.fileConfig(settings.logging_conf)
    _banner()
    uvicorn.run(
        "fairs_bg.app:generate_app",
        factory=True,
        workers=settings.workers,
        log_config=settings.logging_conf,
    )
