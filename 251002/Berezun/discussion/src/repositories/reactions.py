
from cassandra.query import SimpleStatement
from src.db.cassandra import get_session

session = get_session()


def create_reaction(id: int, newsId: int, content: str, state: str):
    query = """
    INSERT INTO tbl_reaction (id, newsId, content, state)
    VALUES (%s, %s, %s, %s)
    """
    session.execute(query, (id, newsId, content, state))
    return {"id": id, "newsId": newsId, "content": content, "state": state}

def get_reaction_by_id(id):
    query = """
    SELECT id, newsId, content, state
    FROM tbl_reaction
    WHERE id = %s
    """
    row = session.execute(query, (id,)).one()
    return row._asdict() if row else None

def get_all_reactions():
    query = "SELECT id, newsId, content, state FROM tbl_reaction"
    rows = session.execute(query)
    return [r._asdict() for r in rows]

def delete_reaction(id):
    query = f"DELETE FROM tbl_reaction WHERE id = %s"
    session.execute(query, (id,))

def update_reaction(id, newsId, content, state):
    query = """
    UPDATE tbl_reaction
    SET newsId = %s, content = %s, state = %s
    WHERE id = %s
    """
    session.execute(query, (newsId, content, state, id))
    return get_reaction_by_id(id)
