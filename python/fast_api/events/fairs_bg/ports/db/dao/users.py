from typing import Tuple
from fastapi import Depends
from sqlalchemy.orm import mapped_column, Mapped
from sqlalchemy import Select
from sqlalchemy.exc import IntegrityError
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.user.model import User, UserDao
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select

from fairs_bg.ports.db.sqlalchemy import Base, BaseAlchemyDao, get_db


class UserDB(Base):
    __tablename__ = "users"

    id: Mapped[int] = mapped_column(primary_key=True, index=True)
    name: Mapped[str] = mapped_column(unique=True, index=True)
    email: Mapped[str] = mapped_column(unique=True, index=True)


class UserAlchemy(UserDao, BaseAlchemyDao[User, type[UserDB]]):
    def __init__(self, db: AsyncSession) -> None:
        super().__init__(db, UserDB)

    async def findByEmail(self, mail: str) -> User | None:
        stmt: Select[Tuple[UserDB]] = select(UserDB).where(UserDB.email == mail)
        raw_user = await self.db.scalar(stmt)
        if not raw_user:
            return None
        return self._adapt_from_db(raw_user)

    def conver_error(self, error: Exception) -> FairsException:
        if isinstance(error, IntegrityError) and "unique_email" in str(error):
            return FairsException(
                ErrorCode.USER_DUPLICATED_EMAIL,
                "The email is already present in our system. To update the user you need to know the id of the user.",
            )
        return super().conver_error(error)

    @classmethod
    def to_business(cls, user: UserDB) -> User:
        return User(**user.__dict__)

    def _adapt_from_db(self, user: UserDB) -> User:
        return UserAlchemy.to_business(user)

    @classmethod
    def from_business(cls, user: User) -> UserDB:
        return UserDB(**user.model_dump())

    async def _adapt_from_business(self, user: User) -> UserDB:
        return UserAlchemy.from_business(user)


def get_user_dao(db: AsyncSession = Depends(get_db)) -> UserAlchemy:
    return UserAlchemy(db)
