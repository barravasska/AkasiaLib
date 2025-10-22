package model

class BukuCetak(
    id: String,
    judul: String,
    penulis: String,
    tahun: Int,
    kategori: String,
    val jumlahHalaman: Int,
    var stok: Int
) : Buku(id, judul, penulis, tahun, kategori) {

    override fun info(): String {
        return "📘 Buku Cetak: $judul oleh $penulis ($tahun), Hal: $jumlahHalaman, Stok: $stok"
    }
}
