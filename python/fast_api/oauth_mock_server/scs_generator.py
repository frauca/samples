#how to generate JWKS from public key in ec with python
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import ec
import os
import json
import base64

private_key_file = "keys/scs_key.pem"

# Check if the private key file exists
if os.path.exists(private_key_file):
    # Load the private key from the existing file
    with open(private_key_file, "rb") as f:
        private_key = serialization.load_pem_private_key(f.read(), password=None)
else:
    # Generate a new EC key pair if the file doesn't exist
    private_key = ec.generate_private_key(ec.SECP256R1())

    # Serialize the private key to PEM format and save it to a file
    with open(private_key_file, "wb") as f:
        private_key_pem = private_key.private_bytes(
            encoding=serialization.Encoding.PEM,
            format=serialization.PrivateFormat.TraditionalOpenSSL,
            encryption_algorithm=serialization.NoEncryption()
        )
        f.write(private_key_pem)

# Get the public key from the private key
public_key = private_key.public_key()

# Convert the public key to JWKS format
jwks = {
    "keys": [
        {
            "kty": "EC",
            "crv": "P-256",  # Replace with your curve if different
            "x": public_key.public_numbers().x,
            "y": public_key.public_numbers().y,
        }
    ]
}

# Serialize the JWKS to a JSON string
jwks_json = json.dumps(jwks, indent=4)

print(jwks_json)

print(jwks)

x_b64 = base64.urlsafe_b64encode(public_key.public_numbers().x.to_bytes((public_key.public_numbers().x.bit_length() + 7) // 8, byteorder='big')).rstrip(b'=').decode('utf-8')
y_b64 = base64.urlsafe_b64encode(public_key.public_numbers().y.to_bytes((public_key.public_numbers().y.bit_length() + 7) // 8, byteorder='big')).rstrip(b'=').decode('utf-8')

jwks = {
    "keys": [
        {
            "kty": "EC",
            "crv": "P-256",  # Replace with your curve if different
            "x": x_b64,
            "y": y_b64,
        }
    ]
}

jwks_json = json.dumps(jwks, indent=4)

print(jwks_json)