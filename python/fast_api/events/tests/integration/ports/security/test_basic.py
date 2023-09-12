from wsgiref import headers
from httpx import AsyncClient

import pytest

from tests.integration.conftest import auth_header


@pytest.mark.asyncio
async def test_unauthorized(unauth_client: AsyncClient) -> None:
    response = await unauth_client.get(
        "/users/1",
        headers={},
    )
    assert response.status_code == 401

    response = await unauth_client.get(
        "/users/1",
        headers=auth_header("invalid@mail", "invalid password"),
    )
    assert response.status_code == 401
