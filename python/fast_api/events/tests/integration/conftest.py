

import asyncio
from typing import AsyncGenerator, Generator
from fastapi import FastAPI
from httpx import AsyncClient
import pytest_asyncio
from testcontainers.postgres import PostgresContainer
import pytest
from fairs_bg.app import app_from
from alembic.config import Config as AlembicConfig
from alembic import command as alembic_command
from fairs_bg.ports.config.settings import FairsSettings
from sqlalchemy.ext.asyncio import AsyncSession

from fairs_bg.ports.db.sqlalchemy import session
@pytest.fixture(scope="session", autouse=True)
def postgres_container()->Generator[PostgresContainer, None, None]:
    postgres = PostgresContainer("postgres:12.6")
    postgres.start()
    yield postgres
    postgres.stop()

@pytest.fixture(scope="session", autouse=True)
def app_settings(postgres_container:PostgresContainer)->FairsSettings:
    settings = FairsSettings()
    postgres_container.driver = "asyncpg"
    settings.database_url = postgres_container.get_connection_url()
    alembic_cfg = AlembicConfig()
    alembic_cfg.set_main_option("sqlalchemy.url", settings.database_url)
    alembic_cfg.set_main_option("script_location", "fairs_bg/ports/db/migrations")
    alembic_command.upgrade(alembic_cfg, "head")
    return settings

@pytest.fixture(scope="session", autouse=True)
def app(app_settings:FairsSettings)->FastAPI:
    return app_from(app_settings)

@pytest_asyncio.fixture(scope="session", autouse=True)
async def client(app:FastAPI) -> AsyncGenerator[AsyncClient, None]:
    async with AsyncClient(app=app, base_url="http://test") as client:
        yield client

@pytest.fixture(scope="session", autouse=True)
def db(app_settings:FairsSettings) -> AsyncSession:
    return session(app_settings)

@pytest.fixture(scope="session")
def event_loop():
    return asyncio.get_event_loop()