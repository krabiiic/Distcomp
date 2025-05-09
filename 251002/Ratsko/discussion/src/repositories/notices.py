
from cassandra.query import SimpleStatement
from src.db.cassandra import get_session

session = get_session()


def create_notice(id: int, issueId: int, content: str, state: str):
    query = """
    INSERT INTO tbl_notice (id, issueId, content, state)
    VALUES (%s, %s, %s, %s)
    """
    session.execute(query, (id, issueId, content, state))
    return {"id": id, "issueId": issueId, "content": content, "state": state}

def get_notice_by_id(id):
    query = """
    SELECT id, issueId, content, state
    FROM tbl_notice
    WHERE id = %s
    """
    row = session.execute(query, (id,)).one()
    return row._asdict() if row else None

def get_all_notices():
    query = "SELECT id, issueId, content, state FROM tbl_notice"
    rows = session.execute(query)
    return [r._asdict() for r in rows]

def delete_notice(id):
    query = f"DELETE FROM tbl_notice WHERE id = %s"
    session.execute(query, (id,))

def update_notice(id, issueId, content, state):
    query = """
    UPDATE tbl_notice
    SET issueId = %s, content = %s, state = %s
    WHERE id = %s
    """
    session.execute(query, (issueId, content, state, id))
    return get_notice_by_id(id)
