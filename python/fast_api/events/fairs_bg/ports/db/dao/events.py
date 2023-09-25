from sqlalchemy import ForeignKey, PrimaryKeyConstraint, Result, select
from sqlalchemy.orm import mapped_column, Mapped, relationship, joinedload
from sqlalchemy.ext.asyncio import AsyncSession
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.events.model import Event
from fairs_bg.business.user.model import User
from fairs_bg.ports.db.dao.users import UserAlchemy, UserDB
from fairs_bg.ports.db.sqlalchemy import Base, BaseAlchemyDao
from datetime import datetime


class EventDB(Base):
    __tablename__ = "events"

    id: Mapped[int] = mapped_column(primary_key=True, index=True)
    name: Mapped[str] = mapped_column(unique=True, nullable=False)
    begining: Mapped[datetime] = mapped_column(nullable=False)
    ending: Mapped[datetime] = mapped_column(nullable=False)
    organizer_id: Mapped[int] = mapped_column(ForeignKey("users.id"))
    organizer: Mapped["UserDB"] = relationship()


class RetailerDB(Base):
    __tablename__ = "retailers"

    retailer_id: Mapped[int] = mapped_column(
        ForeignKey("users.id"),
        primary_key=True,
    )
    retailer: Mapped["UserDB"] = relationship()
    event_id: Mapped[int] = mapped_column(
        ForeignKey("events.id"),
        primary_key=True,
    )
    event: Mapped["EventDB"] = relationship()


class EventAlchemy(BaseAlchemyDao[Event, EventDB]):
    def __init__(self, db: AsyncSession, user_dao: UserAlchemy) -> None:
        super().__init__(db, EventDB)
        self.user_dao = user_dao

    async def findByOrganizer(self, organizer_id: int) -> list[Event]:
        query = (
            select(EventDB)
            .where(EventDB.organizer_id == organizer_id)
            .options(joinedload(EventDB.organizer))
        )
        result = await self.db.execute(query)
        return [self._adapt_from_db(event_db) for event_db in result.scalars()]

    async def addRetailer(self, retailer: User, event: Event) -> None:
        retailer_db = UserAlchemy.from_business(retailer)
        event_db = self._adapt_from_business(event)
        relation = RetailerDB(ratailer=retailer_db, event=event_db)
        self.db.add(relation)
        await self.db.flush()

    def _adapt_from_db(self, event: EventDB) -> Event:
        return Event(
            id=event.id,
            name=event.name,
            begining=event.begining,
            ending=event.ending,
            organizer_id=event.organizer_id,
        )

    async def _adapt_from_business(self, event: Event) -> EventDB:
        if not event.organizer_id:
            raise FairsException(
                ErrorCode.UNEXPECTED,
                f"I did not see this comming. An event {event.id} has no organizer, and I really put all my efforts to this not to happen",
            )
        organizer_db = await self.user_dao.findByIdDB(event.organizer_id)
        return EventDB(
            id=event.id,
            name=event.name,
            begining=event.begining,
            ending=event.ending,
            organizer=organizer_db,
        )
