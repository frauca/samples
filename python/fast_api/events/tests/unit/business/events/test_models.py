from datetime import datetime, timedelta
import pytest
from fairs_bg.business.errors.error_code import ErrorCode

from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.business.events.model import Event
from fairs_bg.business.user.model import User
from tests.conftest import unsaved_event, unsaved_user


def test_date_validations(user: User) -> None:
    with pytest.raises(FairsException) as failure:
        Event(
            id=1,
            name="event",
            begining=datetime.now(),
            ending=datetime.now() - timedelta(days=3),
            organizer=user,
        )
    error = failure.value
    assert error.type == ErrorCode.EVENT_DATE_ENDS_BEFORE_START
    assert (
        error.type.title
        == "Whoa there, time traveler! Our app seems to think you're starting events in reverse. We promise we're not operating in a 'Back to the Future' mode. Let's try that again from the beginning!"
    )
    assert error.type.code == "020001"
    assert "must be after start date" in error.detail


def test_event_json(user: User) -> None:
    event = unsaved_event(owner=user)
    event.begining = datetime(year=1970, month=1, day=1)
    json = event.model_dump_json()
    assert "1970-01-01T00:00:00" in json
