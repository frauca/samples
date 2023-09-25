from fairs_bg.business.events.model import Event
from fairs_bg.business.ports.persistance import ModelDao


class EventDao(ModelDao[Event]):
    pass


class EventService:
    def __init__(self, dao: EventDao) -> None:
        self.dao = dao

    async def save(self, event: Event) -> Event:
        return await self.dao.save(event)
