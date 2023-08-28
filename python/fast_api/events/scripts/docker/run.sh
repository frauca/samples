echo "Loading database changes"
alembic upgrade head
echo "Starting app"
fairs-bg