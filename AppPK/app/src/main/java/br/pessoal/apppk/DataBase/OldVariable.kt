package br.pessoal.apppk.DataBase


val userCredentials = mutableMapOf(
    "l.serenato" to UserProfile("Lucas Albano Ribas Serenato", "1234", "lk.serenato@example.com","adm"),
    "lorenna.j" to UserProfile("Lorenna Judit", "qwe123", "lohve@example.com","user"),
    "cesar.a" to UserProfile("Cesar Augusto Serenato", "senha", "cesar@example.com","coord"),
    "mirian.a" to UserProfile("Mirian Albano Ribas", "senha", "mirian@example.com","user")
)

data class UserProfile(
    val fullName: String,
    val password: String,
    val email: String,
    val accessLevel: String
)