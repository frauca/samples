"""04 adding events

Revision ID: e0a0418ccebe
Revises: ba7657077761
Create Date: 2023-09-12 22:30:19.579774

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = "e0a0418ccebe"
down_revision: Union[str, None] = "ba7657077761"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "events",
        sa.Column("id", sa.Integer, primary_key=True),
        sa.Column("name", sa.String(50), nullable=False, unique=True),
        sa.Column("organizer_id", sa.Integer, nullable=False),
        sa.Column("begining", sa.DateTime, nullable=False),
        sa.Column("ending", sa.DateTime, nullable=False),
    )
    op.create_foreign_key(
        "fk_events_organizer", "events", "users", ["organizer_id"], ["id"]
    )

    op.create_table(
        "retailers",
        sa.Column("retailer_id", sa.Integer, nullable=False),
        sa.Column("event_id", sa.Integer, nullable=False)
    )
    op.create_unique_constraint("unique_retailers_event_retailer","retailers",["retailer_id","event_id"])
    
    op.create_foreign_key(
        "fk_retailers_retailer", "retailers", "users", ["retailer_id"], ["id"]
    )
    op.create_foreign_key(
        "fk_retailers_event", "retailers", "events", ["event_id"], ["id"]
    )


def downgrade() -> None:
    op.drop_table("events")
    op.drop_table("retailers")
