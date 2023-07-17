# 
FROM python:3.9

# 
WORKDIR /code

# 
COPY ./requirements.txt /code/requirements.txt

# 
RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt
RUN pip install alembic

# 
COPY ./app /code/app
COPY ./alembic.ini /code/alembic.ini
COPY ./scripts/docker/run.sh /code/run.sh
RUN chmod +x /code/run.sh

# 
CMD /code/run.sh