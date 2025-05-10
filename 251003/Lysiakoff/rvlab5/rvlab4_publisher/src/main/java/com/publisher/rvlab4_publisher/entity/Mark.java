package com.publisher.rvlab4_publisher.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_mark")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
}
