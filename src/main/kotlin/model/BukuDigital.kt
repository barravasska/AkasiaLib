package model

class BukuDigital(
    id: String,
    judul: String,
    penulis: String,
    tahun: Int,
    kategori: String,
    val ukuranFileMb: Double,
    val formatDigital: String,
) : Buku(id, judul, penulis, tahun, kategori) {

    override fun info(): String {
        return "💾 Ebook: $judul ($formatDigital, ${ukuranFileMb}MB) oleh $penulis"
    }
}
