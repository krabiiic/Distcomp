package by.kukhatskavolets.entities

data class Mark(
    override var id: Long = 0L,
    val name: String
) : Identifiable