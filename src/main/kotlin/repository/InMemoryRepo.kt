package repository

/**
 * Interface Repository (Kontrak)
 * Sesuai spesifikasi, kita tambahkan 'delete'.
 */
interface Repository<T> {
    fun save(entity: T) // Create & Update
    fun findAll(): List<T>
    fun findById(id: String): T?
    fun delete(id: String): Boolean
}

class InMemoryRepo<T>(private val getId: (T) -> String) : Repository<T> {
    private val storage = mutableMapOf<String, T>()

    override fun save(entity: T) {
        storage[getId(entity)] = entity
    }

    override fun findAll(): List<T> = storage.values.toList()

    override fun findById(id: String): T? = storage[id]

    override fun delete(id: String): Boolean {
        return storage.remove(id) != null
    }
}