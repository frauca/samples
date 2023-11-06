import jwt
import json

kid="1438289820780"
# Load the private key
private_key_file = "keys/scs_key.pem"

with open(private_key_file, "rb") as f:
    private_key = f.read()

# Define the payload for your JWT
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
token = jwt.encode(payload, private_key, algorithm="ES256",headers={"kid": kid})

print("Generated JWT:")
print(token)