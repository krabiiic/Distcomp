package by.kukhatskavolets.publisher.entities

import by.kukhatskavolets.publisher.entities.Tweet
import jakarta.persistence.*

@Entity
@Table(name = "tbl_user", schema = "public")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, unique = true, length = 64)
    val login: String = "",

    @Column(nullable = false, length = 128)
    val password: String = "",

    @Column(nullable = false, length = 64)
    val firstname: String = "",

    @Column(nullable = false, length = 64)
    val lastname: String = "",

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val tweets: MutableList<Tweet> = mutableListOf()
)