
from typing import Callable
from unittest.mock import MagicMock, patch
import pytest
from sqlalchemy.orm import Session
from fairs_bg.ports.db.dao.users import UserAlchemy
from fairs_bg.ports.db.sqlalchemy import transactional


@patch("sqlalchemy.orm.Session")
def test_commit_on_success(con:MagicMock|Session)->None:
    process:Callable[[],str] = lambda: "text result"
    dao = UserAlchemy(con)

    result = transactional(dao,process)

    assert result == "text result"
    con.commit.assert_called_once()
    con.rollback.assert_not_called()

@patch("sqlalchemy.orm.Session")
def test_rollback_on_error(con:MagicMock|Session)->None:
    def raise_error()->str:
        raise Exception("something has happend")
    dao = UserAlchemy(con)
    
    with pytest.raises(Exception, match="something has happend"):
        transactional(dao,raise_error)
    
    con.commit.assert_not_called()
    con.rollback.assert_called_once()
