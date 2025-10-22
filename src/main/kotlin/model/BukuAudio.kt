package model

class BukuAudio(
    id: String,
    judul: String,
    penulis: String,
    tahun: Int,
    kategori: String,
    val durasiMenit: Int,
    val narrator: String,
    val ukuranFileMb: Double
) : Buku(id, judul, penulis, tahun, kategori) {

    override fun info(): String {
        return "🎧 Buku Audio: $judul (Narrator: $narrator, ${durasiMenit} menit, ${ukuranFileMb}MB)"
    }
}