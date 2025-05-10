package com.rita.publisher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_tweet")
public class Tweet {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="writer_id", nullable = false)
    private Writer writer;
    @Column(nullable = false,unique = true)
    private String title;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    @ManyToMany(mappedBy = "tweets")
    private Set<Sticker> stickers=new HashSet<>();
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tweet",
//            cascade = CascadeType.ALL)
//    private List<Message> messages=new ArrayList<Message>();
}
