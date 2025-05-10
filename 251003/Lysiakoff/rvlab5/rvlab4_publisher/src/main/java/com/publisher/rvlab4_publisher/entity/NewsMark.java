package com.publisher.rvlab4_publisher.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_news_mark")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewsMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long news_id;

    @Column
    private Long mark_id;
}
