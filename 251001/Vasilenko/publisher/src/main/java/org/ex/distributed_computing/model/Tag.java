package org.ex.distributed_computing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends AbstractEntity {

    @Column(name = "name", nullable = false, unique = true, length = 32)
    private String name;
}
