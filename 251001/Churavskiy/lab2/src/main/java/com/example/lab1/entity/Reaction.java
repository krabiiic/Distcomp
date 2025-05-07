package com.example.lab1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tbl_reaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reaction implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @NotBlank
    @Size(min = 2, max = 2048)
    @Column(nullable = false, length = 2048)
    private String content;
}
