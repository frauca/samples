import asyncio
from base64 import b64encode
from typing import AsyncGenerator, Generator
from fastapi import Depends, FastAPI
from httpx import AsyncClient
import pytest_asyncio
from testcontainers.postgres import PostgresContainer
import pytest
from fairs_bg.app import app_from
from alembic.config import Config as AlembicConfig
from alembic import command as alembic_command
from fairs_bg.business.user.model import User
from fairs_bg.ports.config.settings import FairsSettings
from sqlalchemy.ext.asyncio import AsyncSession

from fairs_bg.ports.db.sqlalchemy import session
from fairs_bg.ports.routes.services.users import UserCommandService, get_user_service


@pytest.fixture(scope="session", autouse=True)
def postgres_container() -> Generator[PostgresContainer, None, None]:
    postgres = PostgresContainer("postgres:12.6")
    postgres.start()
    yield postgres
    postgres.stop()


@pytest.fixture(scope="session", autouse=True)
def app_settings(postgres_container: PostgresContainer) -> FairsSettings:
    settings = FairsSettings()
    postgres_container.driver = "asyncpg"
    settings.database_url = postgres_container.get_connection_url()
    alembic_cfg = AlembicConfig()
    alembic_cfg.set_main_option("sqlalchemy.url", settings.database_url)
    alembic_cfg.set_main_option("script_location", "fairs_bg/ports/db/migrations")
    alembic_command.upgrade(alembic_cfg, "head")
    return settings


@pytest.fixture(scope="session", autouse=True)
def app(app_settings: FairsSettings) -> FastAPI:
    return app_from(app_settings)


@pytest_asyncio.fixture(scope="session", autouse=True)
async def client(app: FastAPI, user: User) -> AsyncGenerator[AsyncClient, None]:
    async with AsyncClient(
        app=app,
        base_url="http://test",
        headers=auth_header(user.email, "DoNotTellNoOne"),
    ) as client:
        yield client


@pytest_asyncio.fixture(scope="session", autouse=True)
async def unauth_client(app: FastAPI, user: User) -> AsyncGenerator[AsyncClient, None]:
    async with AsyncClient(app=app, base_url="http://test") as client:
        yield client


@pytest.fixture(scope="session", autouse=True)
def db(app_settings: FairsSettings) -> AsyncSession:
    return session(app_settings)


@pytest.fixture(scope="session")
def event_loop():
    return asyncio.get_event_loop()


def auth_header(user: str, password: str) -> dict:
    auth = b64encode(f"{user}:{password}".encode("utf-8")).decode("ascii")
    return {"Authorization": f"Basic {auth}"}
