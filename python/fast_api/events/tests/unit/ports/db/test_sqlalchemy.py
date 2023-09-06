
from unittest.mock import AsyncMock, patch
import pytest
from sqlalchemy.ext.asyncio import AsyncSession
from fairs_bg.ports.db.dao.users import UserAlchemy
from fairs_bg.ports.db.sqlalchemy import transactional

@pytest.mark.asyncio
async def test_commit_on_success()->None:
    con:AsyncMock|AsyncSession = AsyncMock(AsyncSession)
    async def process()-> str:
        return "text result"

    dao = UserAlchemy(con)

    result = await transactional(dao,process())

    assert result == "text result"
    con.commit.assert_called_once()
    con.rollback.assert_not_called()

@pytest.mark.asyncio
async def test_rollback_on_error()->None:
    con:AsyncMock|AsyncSession = AsyncMock(AsyncSession)
    async def raise_error()->str:
        raise Exception("something has happend")
    dao = UserAlchemy(con)
    
    with pytest.raises(Exception, match="something has happend"):
        await transactional(dao,raise_error())
    
    con.commit.assert_not_called()
    con.rollback.assert_called_once()
