from cassandra.cluster import Cluster
from cassandra.query import SimpleStatement

KEYSPACE = "distcomp"

def get_session():
    cluster = Cluster(["cassandra"], port=9042)
    session = cluster.connect()
    session.execute(f"""
    CREATE KEYSPACE IF NOT EXISTS {KEYSPACE}
    WITH replication = {{'class': 'SimpleStrategy', 'replication_factor': '1'}}
    """)
    session.set_keyspace(KEYSPACE)
    return session

def init_db():
    session = get_session()

    session.execute("""
    CREATE TABLE IF NOT EXISTS tbl_notice (
        id bigint PRIMARY KEY,
        issueId int,
        content text,
        state text
    )
    """)

    return session
