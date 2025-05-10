package com.example.publisher.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_label")
public class Label extends BaseEntity {

    @Column(length = 32, nullable = false, unique = true)
    private String name;
}
