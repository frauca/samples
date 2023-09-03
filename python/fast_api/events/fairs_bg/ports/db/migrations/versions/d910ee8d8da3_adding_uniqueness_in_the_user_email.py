"""adding uniqueness in the user email

Revision ID: d910ee8d8da3
Revises: c6d0c9a0321e
Create Date: 2023-09-03 15:38:16.962526

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = 'd910ee8d8da3'
down_revision: Union[str, None] = 'c6d0c9a0321e'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_unique_constraint("unique_email",table_name="users",columns=["email"])


def downgrade() -> None:
    op.drop_constraint("unique_email",table_name="users")
