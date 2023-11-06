import json
import jwt
from jwt import PyJWKClient
from urllib.parse import urlparse

id_token= "eyJhbGciOiJFUzI1NiIsImtpZCI6IjE0MzgyODk4MjA3ODAiLCJ0eXAiOiJKV1QifQ.eyJ0eG4iOiI2NTVmOWFlNy00ZDllLTRiYjAtOTYwMi0zMTRmYjExMWU5NTIiLCJ1c2VyIjp7InJvbGVzIjpbXSwiYXR0cmlidXRlcyI6W119LCJwcm9qZWN0cyI6W3sidHlwZSI6ImVudGVycHJpc2UiLCJuYW1lIjoiU09OWV9NSURPS1VSQV9BS0FTQUtBXzAxIiwiaWQiOiIwMGc2d29kdzNkckdLaXowQjFkNyIsInN0YXR1cyI6IkFDVElWRSIsInNlcnZpY2UiOiJlZGdlX2FpX3NlbnNpbmdfcGxhdGZvcm0iLCJyb2xlcyI6W3siaWQiOiIzODE5ZTg0MS05ODllLTQyZGUtYTY1ZS1lOWYzMmZkMWFlYzYiLCJuYW1lIjoiTWVtYmVyIn0seyJpZCI6IjUwNTQ3OGFjLWQ1MDktNDYwMC05NGIwLWJiMGFhNDAyZjBiZSIsIm5hbWUiOiJBZG1pbiJ9XX0seyJ0eXBlIjoiZW50ZXJwcmlzZSIsIm5hbWUiOiJURVNUIiwiaWQiOiIwMGc5YTZqaXM0YVFSV0RGQjFkNyIsInN0YXR1cyI6IklOVklURUQiLCJzZXJ2aWNlIjoiZWRnZV9haV9zZW5zaW5nX3BsYXRmb3JtIiwicm9sZXMiOltdfV0sImlzcyI6InNzc2F1dGgiLCJzdWIiOiJyb2dlckBtYWlsLmNvbSIsImF1ZCI6WyJhaXRyaW9zLWdjcyJdfQ.7QqahIs7tM-IVhgb2WszGzVyUaSzyga_py5245-cOAywzcToljAuG4SsVwJ2EKcadWpb0SPk1NU6T11sKLXSsA"
raw_token = jwt.decode(id_token, options={"verify_signature": False})

jwks_uri = "http://localhost:5000/oauth/jwks"
jwks_client = PyJWKClient(jwks_uri)
signing_key = jwks_client.get_signing_key_from_jwt(id_token)
print(f"issuer {signing_key.key}")
claims = jwt.decode(id_token,
                                 signing_key.key,
                                 algorithms=["ES256"],
                                 audience=['aitrios-gcs'],
                                 issuer='sssauth',
                                 require=["sub", "iss", "aud", "project"]
                                 )
print(json.dumps(claims, indent=4))
