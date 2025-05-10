package com.publisher.rvlab4_publisher.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class News {
    @Id
    @Column
    private Long id;
    @Column
    private String title;
    @Column(nullable = false, length = 2048)
    private String content;
    @Column
    private Long author_id;
    @Column
    private List<UUID> mark_ids;
    @Column
    private List<UUID> message_ids;
}