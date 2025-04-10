package by.kukhatskavolets.entities

import jakarta.persistence.*

@Entity
@Table(name = "tbl_comment", schema = "public")
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweetId", nullable = false)
    val tweet: Tweet = Tweet(),

    @Column(nullable = false, length = 2048)
    val content: String  = ""
)