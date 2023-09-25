from httpx import AsyncClient
import pytest

from fairs_bg.business.user.model import User
from tests.conftest import unsaved_event


@pytest.mark.asyncio
async def test_create_event(client: AsyncClient, user: User) -> None:
    event = unsaved_event(owner=user)
    event_json = event.model_dump_json()
    response = await client.post("/events", json=event_json)

    assert response.status_code == 200
