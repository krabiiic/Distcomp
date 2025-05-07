package by.yelkin.TopicService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_creator")
public class Creator extends BaseEntity {
    private String login;
    private String password;
    private String firstname;
    private String lastname;
}
