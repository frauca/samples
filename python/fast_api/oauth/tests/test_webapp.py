import os
import pickle
import httpx

from commons.api import  get_okta_authorize_sample, get_user_info
from commons.security import SESSIONS_FILE


def test_protected_is_logged()-> None:
    response = httpx.get("http://localhost:8080/protected")

    assert response.status_code == 401

def test_print_uri()->None:
    uri = get_okta_authorize_sample()
    print(uri)

def test_invalid_token() -> None:
    token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5a"
    response = httpx.get("http://localhost:8080/protected",headers={"Authorization":f"Bearer {token}"})
    
    assert response.status_code == 401

def test_none_okta_token() -> None:
    token = "eyJhbGciOiJSUzI1NiIsImtpZCI6InNwV2JJaFlDSDFRY2pFQWFCbXVzSlNWYzh1REVud28teDhPdVBNUll5Vk0iLCJ0eXAiOiJKV1QifQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlzcyI6Imh0dHBzOi8vZGV2LTM0MjEwNTQ3Lm9rdGEuY29tL29hdXRoMi9kZWZhdWx0IiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE3OTQ2NzM0MTF9.UyQDMpGG32Zqlux9tr0jQG5TCjtqp8QTWH3Z5naiyFR7eWdraKp3Qi_27wjEBHbYRx9kSNdqeq_Wymw4KKHTMaE73E1Un_7IIW0B25FdhHSyb-m5OCwdbQnA2j6AXmZXHcl1kvTQjuJLmZLUU8SMUyZmvhBCGBJGlIkQsUTzHnKfPqLv52vqD2nXv9gG7yKPn5L0G0-1Pr2hiS3RyQxTTNSQbVKUTHIdjjMcd2SzKandtcJ3RwIIie2e8f_k5puIRYIoh3cVY4OrcOdrqaUWZUUVxmr0b9reBlNpWPlLUjYwzXun4jLY-w4RBkVtrvYY7D_d_A6X3gjdihmjPbLT8A"
    response = httpx.get("http://localhost:8080/protected",headers={"Authorization":f"Bearer {token}"})
    
    assert response.status_code == 401
    

def test_access_protected() -> None:
    
    token = "eyJraWQiOiJzcFdiSWhZQ0gxUWNqRUFhQm11c0pTVmM4dURFbndvLXg4T3VQTVJZeVZNIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIwMHViNXh0ejkyVURsRHJhazVkNyIsIm5hbWUiOiJ1c2VyMSBmYWtlIiwidmVyIjoxLCJpc3MiOiJodHRwczovL2Rldi0zNDIxMDU0Ny5va3RhLmNvbS9vYXV0aDIvZGVmYXVsdCIsImF1ZCI6IjBvYXQ1ejB2MzR2dGVna1c2NWQ2IiwiaWF0IjoxNjk0NjY5ODExLCJleHAiOjE2OTQ2NzM0MTEsImp0aSI6IklELkNOVmxadHlteWNsVXpjUGNVOWphaGF1Z2Jva05XUHM1YXlTOE5OVmQxNzQiLCJhbXIiOlsicHdkIl0sImlkcCI6IjAwb3Q1ODlxNGRmd3hlZzVtNWQ2IiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlcjFAZmFrZS51c2VyLmNvbSIsImF1dGhfdGltZSI6MTY5NDY2OTc5MSwiYXRfaGFzaCI6IkM3UlpWNXlIdjFIVVQxLWlCVFlWaVEiLCJzdWJfaWQiOiJ1c2VyMUBmYWtlLnVzZXIuY29tIn0.lH_0rK_Q4f0pP_dsBXzWOV8KDCHBT35bLoTFofbRITMHbSuwZbFtchqyqL4rdibsXjGPD_wX571wzxKNLKbmLtfJeS6idNwIFGfntzMPf5SNEw6wrQppKkRIugsegYk1enjinHBg--5VDyIzPwozwt0e9Ccr8LsjbmqPzz0ZKWFYeOVHW2EG4cmhjB3sdiFb88NbghLTZbCovf9oBQBlxfd13eMjd2M6F1Zc_EzKVP4AxvcI25b4hbYxFHqBziobiDpwv2kUwBU_84YqhK-vEhXe9_9eiTzhZXeVHjVZ4kADNabCn_68FvESToEOqHx8YZILDQNkIeESy2hf28fgUw"

    response = httpx.get("http://localhost:8080/protected",headers={"Authorization":f"Bearer {token}"})
    
    assert response.status_code == 200
    assert os.environ['OKTA_USER'] in response.text


def test_get_user_info()->None:
    token = "eyJraWQiOiJzcFdiSWhZQ0gxUWNqRUFhQm11c0pTVmM4dURFbndvLXg4T3VQTVJZeVZNIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULjluMXBUbEFRak5VUHZ3UDNNajhQSTRnbkhiTU1zcVY3dmN5aVdHWG1MZlkiLCJpc3MiOiJodHRwczovL2Rldi0zNDIxMDU0Ny5va3RhLmNvbS9vYXV0aDIvZGVmYXVsdCIsImF1ZCI6ImFwaTovL2RlZmF1bHQiLCJpYXQiOjE2OTQ2MzU2NTEsImV4cCI6MTY5NDYzOTI1MSwiY2lkIjoiMG9hdDV6MHYzNHZ0ZWdrVzY1ZDYiLCJ1aWQiOiIwMHViNXh0ejkyVURsRHJhazVkNyIsInNjcCI6WyJwcm9maWxlIiwib3BlbmlkIl0sImF1dGhfdGltZSI6MTY5NDYzMjI0Nywic3ViIjoidXNlcjFAZmFrZS51c2VyLmNvbSJ9.bzmDvjNfOsMpp6nobs6JckYIBsarhYbF2y_SsKP7xiVatXTrPer1Ba5Qi85Zx-HXNt3nxVE1xQAo6HoieM0whriQykx54PBwYuTJ7cVvxXpPhAIKfHdQ2i9C4yATRHdheC9hpT0tUZLwOBUVk3zDRy8SzFUUH_BC6c-NxKZ4czlQREC1J9TLYEU8y4BbFbp_scfxzuD9MWkAJowDvVGGaqmxYHFUsiKT6U8ngirrLqiql9FxtY4q0OtPbGZvF_u_ZmvvnSMTM-DwnLWbOuOugoVegAEfAxyY48V9ibGzAAsucMIZm9IZcOR_2zhS0zzL8mgA5eVXU7shlsG9g2X0RQ"

    userinfo_response = httpx.get(get_user_info(),
                                     headers={'Authorization': f'Bearer {token}'})
    user_json = userinfo_response.json()
    assert user_json.get("sub_id") == "user1@fake.user.com"

def test_webapp_call_resource()->None:
    token = "eyJraWQiOiJzcFdiSWhZQ0gxUWNqRUFhQm11c0pTVmM4dURFbndvLXg4T3VQTVJZeVZNIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIwMHViNXh0ejkyVURsRHJhazVkNyIsIm5hbWUiOiJ1c2VyMSBmYWtlIiwidmVyIjoxLCJpc3MiOiJodHRwczovL2Rldi0zNDIxMDU0Ny5va3RhLmNvbS9vYXV0aDIvZGVmYXVsdCIsImF1ZCI6IjBvYXQ1ejB2MzR2dGVna1c2NWQ2IiwiaWF0IjoxNjk0Njg3MTQzLCJleHAiOjE2OTQ2OTA3NDMsImp0aSI6IklELkt0cFNsWXpZWmQyRDNWSVZQeWNmZmFnQnNuM2d4aVRoMWY5NGxwMkhGRlkiLCJhbXIiOlsicHdkIl0sImlkcCI6IjAwb3Q1ODlxNGRmd3hlZzVtNWQ2IiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlcjFAZmFrZS51c2VyLmNvbSIsImF1dGhfdGltZSI6MTY5NDY2OTc5MSwiYXRfaGFzaCI6InZJNWhsbTBIY3lzVnNzWUxqX1EtaVEiLCJzdWJfaWQiOiJ1c2VyMUBmYWtlLnVzZXIuY29tIn0.itRSX5dbLB2ny-bbqi-efsUoGKYcPU3z1BqM8nOBFhvmIphs5no6J9nDmlLdA3EtdAw-_QenjPs7A1BxQ_oGH6qnBOBvud1dTKHvJTQoOc6hF4xyMdTR93dK7XHj_mDk49x_iM8CxpAEZlJH4osyUHVXE4Ph20XRzVldVHZc_tDhEo3oXj53WlQ1URKJLrM6zpy6L5zdv4gAjpTm3co39R4PO7EOeEZq0t7SS0OvQ1XHchQ3lVT8ujdUFfRXJj40_ffwe7HSVQG0qty5wvQTBNXmUa9PPZLod7m_aiMU5yjxs60b1yNR8jUd-dxH4tiPK4Jj4GYpJUN-SbdeguBlFw"
    response = httpx.get("http://localhost:8080/call-resource",headers={"Authorization":f"Bearer {token}"})
    
    assert response.status_code == 200
    assert os.environ['OKTA_USER'] in response.text

def test_create_empty_sessions()->None:
    sessions:dict[str,str]={}
    with open(SESSIONS_FILE, 'wb') as f:
        pickle.dump(sessions, f)