package by.kukhatskavolets.entities

data class User(
    override var id: Long = 0L,
    val login: String,
    val password: String,
    val firstname: String,
    val lastname: String
) : Identifiable