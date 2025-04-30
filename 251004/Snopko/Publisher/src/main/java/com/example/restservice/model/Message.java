package com.example.restservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_message")
@Data
public class Message implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(length = 2048)
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageState state = MessageState.PENDING;

    public enum MessageState {
        PENDING, APPROVE, DECLINE
    }
}
