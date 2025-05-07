using System;
using FluentMigrator;

namespace Infrastructure.Migrations;

[Migration(20250311101010, TransactionBehavior.None)]
public class TweetMarkersRelation : Migration
{
    public override void Up()
    {
        const string relation_up = """
ALTER TABLE m2m_tweet_marker
DROP CONSTRAINT fk_m2m_tweet_marker_to_tweet,
DROP CONSTRAINT fk_m2m_tweet_marker_to_marker;

ALTER TABLE m2m_tweet_marker
  ADD CONSTRAINT fk_m2m_tweet_marker_to_tweet
      FOREIGN KEY (tweet_id)
      REFERENCES tbl_tweet (id)
      ON DELETE CASCADE,
  ADD CONSTRAINT fk_m2m_tweet_marker_to_marker
      FOREIGN KEY (marker_id)
      REFERENCES tbl_marker (id)
      ON DELETE CASCADE;




CREATE OR REPLACE FUNCTION delete_orphaned_markers()
 RETURNS TRIGGER AS $$
 BEGIN
     IF NOT EXISTS (
         SELECT 1
         FROM m2m_tweet_marker
         WHERE marker_id = OLD.marker_id
     ) THEN
         DELETE FROM tbl_marker
         WHERE id = OLD.marker_id;
     END IF;
 
     RETURN OLD;
 END;
 $$ LANGUAGE plpgsql;
 
 CREATE TRIGGER trigger_delete_orphaned_markers
 AFTER DELETE ON m2m_tweet_marker
 FOR EACH ROW
 EXECUTE FUNCTION delete_orphaned_markers();
""";
        Execute.Sql(relation_up);
    }

    public override void Down()
    {
        const string init_down_script = """
ALTER TABLE m2m_tweet_marker
DROP FOREIGN KEY fk_m2m_tweet_marker_to_tweet,
DROP FOREIGN KEY fk_m2m_tweet_marker_to_marker;

ALTER TABLE m2m_tweet_marker
  ADD CONSTRAINT fk_m2m_tweet_marker_to_tweet
      FOREIGN KEY (tweet_id) 
      REFERENCES tbl_tweet (id)
    ,
  ADD CONSTRAINT fk_m2m_tweet_marker_to_marker
      FOREIGN KEY (marker_id) 
      REFERENCES tbl_marker (id);

DROP TRIGGER m2m_tweet_marker ON m2m_tweet_marker;
DROP FUNCTION delete_orphaned_markers;
""";
        Execute.Sql(init_down_script);
    }
}
