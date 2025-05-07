package by.bsuir.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "tbl_note")
public class Note {

    @PrimaryKeyColumn(name = "country", type = PARTITIONED)
    private String country;

    @PrimaryKeyColumn(name = "issueid", ordinal = 0, type = CLUSTERED)
    private Long issueId;

    @PrimaryKeyColumn(name = "id", ordinal = 1, type = CLUSTERED)
    private Long id;

    private String content;

}
