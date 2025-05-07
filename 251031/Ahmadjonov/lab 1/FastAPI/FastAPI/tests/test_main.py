from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)

def test_create_user():
    response = client.post("/api/v1.0/users/", json={"login": "testuser"})
    assert response.status_code == 201
    assert "id" in response.json()
