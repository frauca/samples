from fairs_bg.business.errors.error_code import ErrorCode


class FairsException(Exception):
    def __init__(self,type: ErrorCode, message:str) -> None:
        super().__init__(message)
        self.type = type
        self.detail = message