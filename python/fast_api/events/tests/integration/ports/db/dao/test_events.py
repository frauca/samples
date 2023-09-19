import pytest
from sqlalchemy.ext.asyncio import AsyncSession, async_sessionmaker
from fairs_bg.business.events.model import Event
from fairs_bg.business.user.model import User
from fairs_bg.ports.db.dao.events import EventAlchemy, EventDB
from datetime import datetime, timedelta
from fairs_bg.ports.db.dao.users import UserAlchemy, UserDB


@pytest.mark.asyncio
async def test_adding_events(db: async_sessionmaker[AsyncSession]) -> None:
    con = db()
    try:
        async with con.begin():
            user = UserDB(name="user1", email="user1@mail.com")
            event = EventDB(
                name="event1",
                begining=datetime.now(),
                ending=datetime.now() + timedelta(days=5),
                organizer=user,
            )
            con.add(user)
            con.add(event)
            await con.flush()

            await con.delete(event)
            await con.delete(user)

            await con.commit()
    finally:
        await con.close()


@pytest.mark.asyncio
async def test_find_bt_organizer(
    db: async_sessionmaker[AsyncSession], unsaved_event: Event
) -> None:
    con = db()
    try:
        async with con.begin():
            unsaved_user = unsaved_event.organizer
            user_dao = UserAlchemy(con)
            event_dao = EventAlchemy(con, user_dao)
            user = await user_dao.save(model=unsaved_user)
            unsaved_event.organizer = user
            event = await event_dao.save(unsaved_event)
            assert user.id and event.id

            user_events = await event_dao.findByOrganizer(organizer_id=user.id)

            assert len(user_events) == 1
            assert user_events[0].id == event.id

            await event_dao.delete(event.id)
            await user_dao.delete(user.id)
    finally:
        await con.close()
