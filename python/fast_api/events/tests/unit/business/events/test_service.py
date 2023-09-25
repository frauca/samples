from unittest.mock import AsyncMock

import pytest
from fairs_bg.business.events.model import Event

from fairs_bg.business.events.service import EventDao, EventService
from tests.conftest import async_return_value, new_event


@pytest.mark.asyncio
async def test_save(event_dao: AsyncMock) -> None:
    unsaved: Event = new_event()
    saved: Event = new_event()
    event_dao.save = async_return_value(saved)
    service: EventService = EventService(event_dao)

    result = await service.save(unsaved)

    event_dao.save.assert_awaited_once_with(unsaved)
    assert result == saved
