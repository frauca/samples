#   lets create a key to sign these tokens with
#   openssl genpkey -out mykey.pem -algorithm rsa -pkeyopt rsa_keygen_bits:2048 
#   # lets generate a public key for it...
#   openssl rsa -in mykey.pem -out mykey.pub -pubout 
#   # make another key so we can test that we cannot decode from it
#   openssl genpkey -out notmykey.pem -algorithm rsa -pkeyopt rsa_keygen_bits:2048 
#   # this is really the key we would be using to try to check the signature
#   openssl rsa -in notmykey.pem -out notmykey.pub -pubout

import jwt

from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization

# Load the key we created
with open("keys/mykey.pem", "rb") as key_file:
    private_key = serialization.load_pem_private_key(
        key_file.read(),
        password=None,
        backend=default_backend()
    )

# The data we're trying to pass along from place to place
data = {'user_id': 1}

# Lets create the JWT token -- this is a byte array, meant to be sent as an HTTP header
jwt_token = jwt.encode(data, key=private_key, algorithm='RS256')

print(f'data {data}')
print(f'jwt_token {jwt_token}')

# Load the public key to run another test...
with open("keys/mykey.pub", "rb") as key_file:
    public_key = serialization.load_pem_public_key(
        key_file.read(),
        backend=default_backend()
    )

# This will prove that the derived public-from-private key is valid
print(f'decoded with public key (internal): {jwt.decode(jwt_token, private_key.public_key(), algorithms=["RS256"])}')
# This will prove that an external service consuming this JWT token can trust the token 
# because this is the only key it will have to validate the token.
print(f'decoded with public key (external): {jwt.decode(jwt_token, public_key, algorithms=["RS256"])}')

payload = {
    "txn": "655f9ae7-4d9e-4bb0-9602-314fb111e952",
    "user": {
        "roles": [],
        "attributes": []
    },
    "projects": [
        {
            "type": "enterprise",
            "name": "SONY_MIDOKURA_AKASAKA_01",
            "id": "00g6wodw3drGKiz0B1d7",
            "status": "ACTIVE",
            "service": "edge_ai_sensing_platform",
            "roles": [
                {
                    "id": "3819e841-989e-42de-a65e-e9f32fd1aec6",
                    "name": "Member"
                },
                {
                    "id": "505478ac-d509-4600-94b0-bb0aa402f0be",
                    "name": "Admin"
                }
            ]
        },
        {
            "type": "enterprise",
            "name": "TEST",
            "id": "00g9a6jis4aQRWDFB1d7",
            "status": "INVITED",
            "service": "edge_ai_sensing_platform",
            "roles": []
        }
    ],
    "iss": "sssauth",
    "sub": "roger@mail.com",
    "aud": [
        "aitrios-gcs"
    ]
}

# Generate the JWT
token = jwt.encode(payload, private_key, algorithm="RS256")

print(token)