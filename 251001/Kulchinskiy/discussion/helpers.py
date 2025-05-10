from cassandra.cluster import Cluster
from django.conf import settings

def get_session():
    cluster = Cluster([settings.CASSANDRA_HOST])
    session = cluster.connect()
    session.set_keyspace(settings.CASSANDRA_DB)
    session.execute("""
        CREATE TABLE IF NOT EXISTS id_generator (
            key text PRIMARY KEY,
            last_id counter
        )
    """)
    return session


def get_next_id(key='comment'):
    session = get_session()

    # Atomic counter update
    session.execute("""
            UPDATE id_generator SET last_id = last_id + 1 WHERE key = %s
        """, [key])

    result = session.execute("""
            SELECT last_id FROM id_generator WHERE key = %s
        """, [key])
    return result.one().last_id
