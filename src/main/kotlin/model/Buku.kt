package model

abstract class Buku(
    val id: String,
    val judul: String,
    val penulis: String,
    val tahun: Int,
    val kategori: String
) {
    abstract fun info(): String
}
