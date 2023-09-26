from datetime import datetime
from httpx import AsyncClient
import pytest

from fairs_bg.business.user.model import User
from fairs_bg.ports.routes.events.service import EventRest
from tests.conftest import unsaved_event


@pytest.mark.asyncio
async def test_create_event(client: AsyncClient, user: User) -> None:
    event = EventRest(**unsaved_event(user).model_dump())
    event_json = event.model_dump_json(exclude_none=True)
    response = await client.post("/events", content=event_json)

    assert response.status_code == 200
