"""Adding default user

Revision ID: ba7657077761
Revises: d910ee8d8da3
Create Date: 2023-09-12 07:10:06.342813

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = "ba7657077761"
down_revision: Union[str, None] = "d910ee8d8da3"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.execute("INSERT INTO users(name,email) VALUES ('first user','first@fake.mail')")


def downgrade() -> None:
    op.execute("DELETE FROM users where name ='first user'")
