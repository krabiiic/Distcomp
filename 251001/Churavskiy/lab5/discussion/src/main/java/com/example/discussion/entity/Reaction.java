package com.example.discussion.entity;

import com.example.discussion.state.ReactionState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.*;

@Table("tbl_reaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {
    @PrimaryKey
    private ReactionKey id;

    @Column
    private String content;

    @Column
    private ReactionState state;
}
