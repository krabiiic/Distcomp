package by.kukhatskavolets.publisher.entities

import jakarta.persistence.*

@Entity
@Table(name = "tbl_mark", schema = "public")
data class Mark(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, unique = true, length = 32)
    val name: String  = "",
)