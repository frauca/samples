echo "Loading database changes"
alembic upgrade head
echo "Starting app"
uvicorn app.main:app --host 0.0.0.0 --port 8001