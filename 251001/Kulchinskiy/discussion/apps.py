from cassandra.cluster import Cluster
from django.apps import AppConfig

from labs_distributed_computing import settings


class DiscussionConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'discussion'

    def ready(self):
        keyspace = settings.DATABASES['cassandra']['NAME']
        hosts = [settings.DATABASES['cassandra']['HOST']]

        # Ensure keyspace exists
        cluster = Cluster(hosts)
        session = cluster.connect()
        session.execute(f"""
                    CREATE KEYSPACE IF NOT EXISTS {keyspace}
                    WITH replication = {{ 'class': 'SimpleStrategy', 'replication_factor': 1 }};
                """)
        session.shutdown()

        # Set up Cassandra connection
        from cassandra.cqlengine import connection
        connection.setup(hosts, keyspace, protocol_version=4)
