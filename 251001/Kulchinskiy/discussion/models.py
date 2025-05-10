from cassandra.cqlengine import columns
from django_cassandra_engine.models import DjangoCassandraModel


class Comment(DjangoCassandraModel):
    id = columns.BigInt(primary_key=True)
    newsId = columns.BigInt()
    content = columns.Text(min_length=2, max_length=2048)

    class Meta:
        db_table = 'tbl_comment'
