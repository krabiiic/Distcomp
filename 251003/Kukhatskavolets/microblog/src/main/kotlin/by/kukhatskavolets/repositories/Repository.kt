package by.kukhatskavolets.repositories

import by.kukhatskavolets.entities.Identifiable

interface Repository<T: Identifiable> {
    fun save(entity: T): T
    fun update(entity: T): T
    fun deleteById(id: Long)
    fun findById(id: Long): T
    fun findAll(): List<T>
}