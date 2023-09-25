from enum import Enum
from typing import Any


GENERIC_TYPE = "00"
USER_TYPE = "01"
EVENT_TYPE = "02"


def _error_code(type: str, specific: str) -> str:
    return type + specific


class ErrorCode(Enum):
    UNEXPECTED = (
        _error_code(GENERIC_TYPE, "0001"),
        "Oops! Something went wrong, but we're on it! Stay tuned for a fix.",
    )
    NOT_FOUND = (
        _error_code(GENERIC_TYPE, "0002"),
        "It seems like we couldn't find what you're looking for.",
    )
    USER_DUPLICATED_EMAIL = (
        _error_code(USER_TYPE, "0001"),
        "Already know this user with another name.",
    )
    EVENT_DATE_ENDS_BEFORE_START = (
        _error_code(EVENT_TYPE, "0001"),
        "Whoa there, time traveler! Our app seems to think you're starting events in reverse. We promise we're not operating in a 'Back to the Future' mode. Let's try that again from the beginning!",
    )
    EVENT_INVALID_FORMAT = (
        _error_code(EVENT_TYPE, "0002"),
        "Ei, look your event twice there is some nasty in it.",
    )

    def __new__(cls, *args: Any, **kwds: Any) -> "ErrorCode":
        value = len(cls.__members__) + 1
        obj = object.__new__(cls)
        obj._value_ = value
        return obj

    def __init__(self, code: str, title: str):
        self.code = code
        self.title = title
