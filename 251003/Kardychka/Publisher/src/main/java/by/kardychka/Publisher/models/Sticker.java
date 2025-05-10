package by.kardychka.Publisher.models;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tbl_sticker")
public class Sticker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "stickers")
    private List<News> newss = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<News> getNewss() {
        return newss;
    }

    public void setNewss(List<News> newss) {
        this.newss = newss;
    }

/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sticker sticker = (Sticker) o;
        return id == sticker.id && name.equals(sticker.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }*/

    public Sticker(String name) {
        this.name = name;
    }

    public Sticker() {
    }
}
