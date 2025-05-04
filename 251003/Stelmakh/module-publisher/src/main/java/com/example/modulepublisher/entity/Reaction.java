package com.example.modulepublisher.entity;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_reaction")
public class Reaction {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "issueId")
    private int issueId;
    private String content;
    @Enumerated(EnumType.STRING)
    private States state;
}
