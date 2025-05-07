package com.example.modulepublisher.entity;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_message")
public class Message {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tweetId")
    private int tweetId;
    private String content;
    @Enumerated(EnumType.STRING)
    private States state;
}
