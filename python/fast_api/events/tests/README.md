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

### Test execution withou async

| users | rps | erors |
|-------|-----|-------|
| 1     | 66  | 0%    |
| 5     | 134 | 0%    |
| 10    | 152 | 12%   |
| 20    | 162 | 35%   |