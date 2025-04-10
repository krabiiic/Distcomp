package by.kukhatskavolets.database

import by.kukhatskavolets.entities.User
import by.kukhatskavolets.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class DatabaseInitializer(
    private val userRepository: UserRepository
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {
        val user = User(
            login = "kuhockovolec@gmail.com",
            password = "12345678",
            firstname = "Артур",
            lastname = "Кухоцковолец"
        )
        userRepository.save(user)
    }
}
