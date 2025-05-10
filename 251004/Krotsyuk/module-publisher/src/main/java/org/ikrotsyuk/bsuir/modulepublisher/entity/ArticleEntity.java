package org.ikrotsyuk.bsuir.modulepublisher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "tbl_article")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private WriterEntity writer;
    @NotBlank
    @Size(min = 2, max = 64)
    private String title;
    @NotBlank
    @Size(min = 4, max = 2048)
    private String content;
    @Column(name = "created")
    private LocalDateTime createdAt;
    @Column(name = "modified")
    private LocalDateTime modifiedAt;

    /*@OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<ReactionEntity> reactions = new LinkedList<>();*/

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "tbl_articles_stickers",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "sticker_id")
    )
    private List<StickerEntity> stickers = new LinkedList<>();

    public void assignStickers(List<StickerEntity> stickerEntityList){
        stickers.addAll(stickerEntityList);
    }
}