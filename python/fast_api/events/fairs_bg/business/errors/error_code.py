from enum import Enum
from typing import Any


GENERIC_TYPE = "00"
def _error_code(type:str,specific:str)-> str:
    return type+specific

class ErrorCode(Enum):
    UNEXPECTED = _error_code(GENERIC_TYPE,"0001"), "Oops! Something went wrong, but we're on it! Stay tuned for a fix."
    NOT_FOUND = _error_code(GENERIC_TYPE,"0002"), "It seems like we couldn't find what you're looking for."

    def __new__(cls, *args:Any, **kwds:Any)->"ErrorCode":
          value = len(cls.__members__) + 1
          obj = object.__new__(cls)
          obj._value_ = value
          return obj
    
    def __init__(self, code:str, title:str):
        self.code = code
        self.title = title