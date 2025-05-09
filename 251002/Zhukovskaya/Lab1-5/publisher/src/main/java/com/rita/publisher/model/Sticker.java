package com.rita.publisher.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_sticker")
public class Sticker {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "tweet_sticker",
            joinColumns = @JoinColumn(name = "sticker_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private Set<Tweet> tweets=new HashSet<>();

}
