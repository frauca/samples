from tests import commons
from locust import HttpUser, task


class UserCreation(HttpUser):
    @task
    def create_get_delete_users(self) -> None:
        name = commons.random_string(20)
        response = self.client.post(
            "/users", json={"id": None, "name": name, "email": f"{name}@user.mail"}
        )
        if response.status_code < 300:
            user_id = response.json()["id"]
            self.client.get(f"/users/{user_id}", name="get user")
            self.client.delete(f"/users/{user_id}", name="delete user")
