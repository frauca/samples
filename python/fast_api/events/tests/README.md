# Run the tests

To run the test

```
pip install -r scripts/requirements/requirements-test.txt 
coverage run -m pytest
coverage report
```

## Loading tests

I want to check the diference on async fast api vs blocking one. I will move to async all the program but first I want to see the values and make a base line.

I will make a loading tests with locust and once moved to async compare the time results and set as a base line

To run locust, first [run fairs-bg](../scripts/README.md) and then run

```
locust -f tests/loading/locustfile.py -H http://127.0.0.1:8000 
```

### Simple user test

We are executing the create_get_delete_users  that just create a user, retrieves it and then delete it.

We did execute it without authentication and syncronously (commit 648a4da)

| users | rps | erors |
|-------|-----|-------|
| 1     | 66  | 0%    |
| 5     | 134 | 0%    |
| 10    | 152 | 12%   |
| 20    | 162 | 35%   |

we have added syncronous (commit ) and run the tests again. I have to say that the results really impacted me:

| users | rps | erors |
|-------|-----|-------|
| 1     | 66  | 0%    |
| 5     | 134 | 0%    |
| 10    | 152 | 12%   |
| 20    | 162 | 35%   |

Really much worst. I would keep with async just for learning purposes but the results somehow hit me hard