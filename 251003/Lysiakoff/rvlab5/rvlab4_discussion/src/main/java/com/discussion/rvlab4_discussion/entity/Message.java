package com.discussion.rvlab4_discussion.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 2048)
    private String content;
    @Column
    private Long newsId;
}
