import abc
from typing import Any, AsyncGenerator, Generic, Type, TypeVar, Coroutine
from fastapi import Request
from sqlalchemy.ext.asyncio import (
    create_async_engine,
    AsyncSession,
    async_sessionmaker,
    AsyncAttrs,
)
from sqlalchemy.orm import DeclarativeBase
from sqlalchemy import select
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.ports.persistance import P, ModelDao
from fairs_bg.ports.config.settings import FairsSettings


def session(settings: FairsSettings) -> async_sessionmaker[AsyncSession]:
    engine = create_async_engine(settings.database_url)
    return async_sessionmaker(engine, expire_on_commit=False)


async def get_db(request: Request) -> AsyncGenerator[AsyncSession, None]:
    db = request.app.state.db_session()
    try:
        async with db.begin():
            yield db
    finally:
        await db.close()


class Base(AsyncAttrs, DeclarativeBase):
    pass


PDB = TypeVar("PDB", bound=Base)
T = TypeVar("T")


class BaseAlchemyDao(ModelDao[P], Generic[P, PDB]):
    def __init__(self, db: AsyncSession, entity_type: PDB) -> None:
        super().__init__()
        self.db: AsyncSession = db
        self.type: PDB = entity_type

    async def findById(self, id: int) -> P | None:
        try:
            el: PDB | None = await self._findByIdDB(id)
            if not el:
                return None
            return self._adapt_from_db(el)
        except Exception as error:
            raise self.conver_error(error)

    async def save(self, model: P) -> P:
        entity = self._adapt_from_business(model)
        self.db.add(entity)
        await self.db.flush()
        return self._adapt_from_db(entity)

    async def delete(self, id: int) -> None:
        entity: PDB | None = await self._findByIdDB(id)
        if entity:
            await self.db.delete(entity)

    async def _findByIdDB(self, id: int) -> PDB | None:
        result = await self.db.execute(select(self.type).where(self.type.id == id))
        return result.scalar_one_or_none()

    @abc.abstractmethod
    def _adapt_from_db(self, user: PDB) -> P:
        raise NotImplementedError

    @abc.abstractmethod
    def _adapt_from_business(self, user: P) -> PDB:
        raise NotADirectoryError

    def conver_error(self, error: Exception) -> FairsException:
        return FairsException(
            ErrorCode.UNEXPECTED,
            f"Dad it's not my fault is another one fault, look the error {type(error)} says: {str(error)}",
        )


async def transactional(dao: ModelDao, process: Coroutine[Any, Any, T]) -> T:
    if not isinstance(dao, BaseAlchemyDao):
        raise FairsException(
            ErrorCode.UNEXPECTED,
            "Breaking news: Our code had a mini rebellion. An inspirated developer took a decision that another one did not see it comming. ",
        )
    try:
        result = await process
        await dao.db.commit()
        return result
    except Exception as error:
        await dao.db.rollback()
        raise dao.conver_error(error)
