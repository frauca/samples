# 
FROM python:3.11

# 
WORKDIR /app

# 
COPY ./pyproject.toml /app/pyproject.toml
COPY ./fairs_bg /app/fairs_bg
COPY ./alembic.ini /app/alembic.ini
COPY ./config.yaml /app/config.yaml
COPY ./logging.conf /app/logging.conf

# 
RUN pip install .

# 
COPY ./scripts/docker/run.sh /code/run.sh
RUN chmod +x /code/run.sh

# 
CMD /code/run.sh