package service

import model.User
import model.Role
import repository.InMemoryRepo
import repository.Repository

class AuthService (private val userRepo: Repository<User>) {
    var currentUser: User? = null
        private set

    fun login(username: String, password: String): User? {
        val user = userRepo.findAll().find{ it.username == username }

        if (user != null && user.password == password) {
        currentUser = user
        return user
        }

        currentUser = null
        return null
    }

    fun logout(){
        currentUser = null
    }

    fun register(id: String, username: String, password: String, role: Role) {
        if (userRepo.findAll().any { it.username == username }){
            println("Info: Username $username sudah terdaftar")
            return
        }
        val newUser = User(id, username, password, role)
        userRepo.save(newUser)
        println("Akun ${role} ($username) Berhasil di tambahkan")
    }
}