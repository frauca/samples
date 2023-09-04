import random
import string
from locust import HttpUser, task

def random_string(length:int)->str:
    return ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(length))

class UserCreation(HttpUser):

    @task
    def create_get_delete(self)->None:
        name = random_string(20)
        response = self.client.post("/users", json={
            "id":None,
            "name":name,
            "email":f"{name}@user.mail"
        })
        if response.status_code < 300:
            user_id = response.json()["id"]
            self.client.get(f"/users/{user_id}", name= "get user")
            self.client.delete(f"/users/{user_id}", name = "delete user")