"""create user table

Revision ID: c6d0c9a0321e
Revises: 
Create Date: 2023-08-27 10:00:04.239380

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = 'c6d0c9a0321e'
down_revision: Union[str, None] = None
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        'users',
        sa.Column('id', sa.Integer, primary_key=True),
        sa.Column('name', sa.String(50), nullable=False),
        sa.Column('email', sa.Unicode(320)),
    )


def downgrade() -> None:
    op.drop_table('users')
