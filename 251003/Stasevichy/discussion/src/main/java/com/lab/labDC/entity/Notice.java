package com.lab.labDC.entity;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.annotation.Id;

@Table("tbl_notice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notice {

    @Id
    @CassandraType(type = CassandraType.Name.UUID)
    private Long id;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String content;

    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long storyId;

}