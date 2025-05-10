python -c "from app.database import get_db; print(get_db)"
uvicorn app.main:app --reload
запуск 
 http://127.0.0.1:8000/docs