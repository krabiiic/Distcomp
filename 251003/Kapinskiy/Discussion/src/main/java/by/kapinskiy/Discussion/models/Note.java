package by.kapinskiy.Discussion.models;


import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;


@Table("tbl_note")
@Data
public class Note implements Serializable {

    @PrimaryKeyClass
    @Data
    public static class NoteKey implements Serializable {
        @PrimaryKeyColumn(name = "country", type = PrimaryKeyType.PARTITIONED)
        private String country;

        @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
        private Long id;

        @PrimaryKeyColumn(name = "issueId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long issueId;

        public NoteKey(Long issueId) {
            this.issueId = issueId;
        }
    }


    @Column("content")
    private String content;

    @PrimaryKey
    private NoteKey key;

}