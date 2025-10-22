package model

import java.time.LocalDate

class Peminjaman(
    val id: String,
    val idAnggota: String,
    val idBuku: String,
    val tanggalPinjam: LocalDate,
    val tanggalJatuhTempo: LocalDate,
    var tanggalKembali: LocalDate? = null
) {

    val isAktif: Boolean
        get() = tanggalKembali == null
    fun tandaiKembali(tanggal: LocalDate) {
        tanggalKembali = tanggal
    }

    override fun toString(): String {
        val kembali = tanggalKembali?.toString() ?: "Belum dikembalikan"
        return "📦 Peminjaman(id=$id, Buku=$idBuku, Anggota=$idAnggota, Pinjam=$tanggalPinjam, Kembali=$kembali)"
    }
}
