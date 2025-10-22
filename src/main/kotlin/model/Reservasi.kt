package model

import java.time.LocalDateTime

class Reservasi(
    val id: String,
    val idAnggota: String,
    val idBuku: String,
    val tanggalReservasi: LocalDateTime = LocalDateTime.now(),
    var status: String = "Menunggu"
) {
    fun ubahStatus(statusBaru: String) {
        status = statusBaru
    }

    override fun toString(): String {
        return "📖 Reservasi(id=$id, Buku=$idBuku, Anggota=$idAnggota, Status=$status)"
    }
}
