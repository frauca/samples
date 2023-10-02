from datetime import datetime
from httpx import AsyncClient
import pytest

from fairs_bg.business.user.model import User
from fairs_bg.ports.routes.events.service import EventRest
from tests.conftest import new_event, unsaved_event, unsaved_user
from tests.integration.conftest import auth_header


@pytest.mark.asyncio
async def test_create_event(client: AsyncClient, user: User) -> None:
    event = EventRest(**unsaved_event(user).model_dump())
    event_json = event.model_dump_json(exclude_none=True)
    response = await client.post("/events", content=event_json)

    assert response.status_code == 200


@pytest.mark.asyncio
async def test_retailer_cannot_add_other_retailer(client: AsyncClient) ->None:
    event = EventRest(**new_event().model_dump())
    event_json = event.model_dump_json(exclude_none=True)
    retailer_1_user = unsaved_user()
    retailer_1 = await client.post("/users", json=unsaved_user().model_dump())
    retailer_1_user.id = int(retailer_1.json()["id"])
    
    response = await client.post("/events", content=event_json)
    event_id = response.json()['id']
    add_retailer_url = f"/events/{event_id}/retailer?retailer_id={retailer_1_user.id}"

    response = await client.post(add_retailer_url,headers=auth_header(retailer_1_user.email))

    assert response.status_code == 403