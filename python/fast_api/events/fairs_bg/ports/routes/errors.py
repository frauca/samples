import logging
from fastapi import HTTPException, Request, Response
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from fairs_bg.business.errors.error_code import ErrorCode

from fairs_bg.business.errors.fairs_error import FairsException


def _get_detail_from(error: Exception) -> str:
    if isinstance(error, (FairsException, HTTPException)):
        return error.detail
    return str(error)


def _get_title_from(error: Exception) -> str | None:
    if isinstance(error, FairsException):
        return error.type.title
    return None


def _get_status_from_code(code: ErrorCode) -> int:
    match code:
        case ErrorCode.NOT_FOUND:
            return 404
        case ErrorCode.USER_DUPLICATED_EMAIL:
            return 400
        case _:
            return 500


def _get_status_from(error: Exception) -> int:
    if isinstance(error, HTTPException):
        return error.status_code
    elif isinstance(error, FairsException):
        return _get_status_from_code(error.type)
    return 500


def _get_code_from(error: Exception) -> str | None:
    if isinstance(error, FairsException):
        return error.type.code
    return None


def _get_name_from(error: Exception) -> str | None:
    if isinstance(error, FairsException):
        return error.type.name
    return None


class ProblemDetails(BaseModel):
    "https://datatracker.ietf.org/doc/html/rfc7807"
    title: str | None
    detail: str | None
    status: int
    instance: str
    code: str | None
    name: str | None

    @staticmethod
    def create_from(error: Exception, instance: str) -> "ProblemDetails":
        return ProblemDetails(
            title=_get_title_from(error),
            detail=_get_detail_from(error),
            status=_get_status_from(error),
            instance=instance,
            code=_get_code_from(error),
            name=_get_name_from(error),
        )


def _get_level_from(detail: ProblemDetails) -> int:
    if detail.status >= 500:
        return logging.ERROR
    elif detail.status >= 400:
        return logging.WARNING
    return logging.INFO


def http_error_handler(req: Request, error: Exception) -> JSONResponse:
    detail = ProblemDetails.create_from(error, req.url.path)
    logging.log(
        _get_level_from(detail),
        f"Detected error on the request {detail.instance}",
        exc_info=error,
    )
    return JSONResponse(detail.model_dump(), status_code=detail.status)
