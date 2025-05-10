package by.kardychka.Publisher.models;


import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_news")
public class News {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private Creator creator;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created")
    private Date created;

    @Column(name = "modified")
    private Date modified;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tbl_news_sticker", joinColumns = @JoinColumn(name = "news_id"),
    inverseJoinColumns = @JoinColumn(name = "sticker_id"))
      private List<Sticker> stickers = new ArrayList<>();
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public void setStickers(List<Sticker> stickers) {
        this.stickers = stickers;
    }

  /*  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return id == news.id && creator.equals(news.creator) && title.equals(news.title) && content.equals(news.content) && created.equals(news.created) && modified.equals(news.modified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, title, content, created, modified);
    }*/
}
