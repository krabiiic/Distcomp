package by.kukhatskavolets.publisher.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_tweet", schema = "public")
data class Tweet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    val user: User = User(),

    @Column(nullable = false, unique = true, length = 64)
    val title: String = "",

    @Column(nullable = false, length = 2048)
    val content: String = "",

    @Column(nullable = false)
    val created: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var modified: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "tweet", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinTable(
        name = "tbl_tweet_mark",
        schema = "public",
        joinColumns = [JoinColumn(name = "tweetId")],
        inverseJoinColumns = [JoinColumn(name = "markId")]
    )
    val marks: MutableSet<Mark> = mutableSetOf()
)
