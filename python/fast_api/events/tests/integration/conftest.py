

from typing import Generator
from fastapi import FastAPI
from testcontainers.postgres import PostgresContainer
from fastapi.testclient import TestClient
import pytest
from fairs_bg.app import app_from
from alembic.config import Config as AlembicConfig
from alembic import command as alembic_command
from fairs_bg.ports.config.settings import FairsSettings
from sqlalchemy.orm import Session

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
    settings.database_url = postgres_container.get_connection_url()
    alembic_cfg = AlembicConfig()
    alembic_cfg.set_main_option("sqlalchemy.url", settings.database_url)
    alembic_cfg.set_main_option("script_location", "fairs_bg/ports/db/migrations")
    alembic_command.upgrade(alembic_cfg, "head")
    return settings

@pytest.fixture(scope="session", autouse=True)
def app(app_settings:FairsSettings)->FastAPI:
    return app_from(app_settings)

@pytest.fixture(scope="session", autouse=True)
def client(app:FastAPI) -> TestClient:
    return TestClient(app)

@pytest.fixture(scope="session", autouse=True)
def db(app_settings:FairsSettings) -> Session:
    return session(app_settings)