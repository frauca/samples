from fastapi import HTTPException
import pytest
from fairs_bg.business.errors.error_code import ErrorCode
from fairs_bg.business.errors.fairs_error import FairsException
from fairs_bg.ports.routes.errors import ProblemDetails


def test_problem_detail_from_exception()->None:
    error = Exception("Error explanation")
    problem_detail = ProblemDetails.create_from(error,"requested/path")

    assert not problem_detail.title 
    assert problem_detail.detail == "Error explanation"
    assert problem_detail.instance == "requested/path"
    assert problem_detail.status == 500
    assert not problem_detail.code
    assert not problem_detail.name

def test_problem_detail_from_http_exception()->None:
    error = HTTPException(417,"Error explanation")
    problem_detail = ProblemDetails.create_from(error,"requested/path")

    assert not problem_detail.title 
    assert problem_detail.detail == "Error explanation"
    assert problem_detail.instance == "requested/path"
    assert problem_detail.status == 417
    assert not problem_detail.code
    assert not problem_detail.name

@pytest.mark.parametrize("code,http_status", [(ErrorCode.UNEXPECTED, 500), (ErrorCode.NOT_FOUND, 404)])
def test_problem_detail_from_fairs_exception(code:ErrorCode,http_status:int)->None:
    error = FairsException(code,"Error explanation")
    problem_detail = ProblemDetails.create_from(error,"requested/path")

    assert problem_detail.title == code.title
    assert problem_detail.detail == "Error explanation"
    assert problem_detail.instance == "requested/path"
    assert problem_detail.status == http_status
    assert problem_detail.code == code.code
    assert problem_detail.name == code.name