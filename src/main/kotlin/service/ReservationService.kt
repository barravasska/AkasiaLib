package service

import model.Anggota
import model.Buku
import model.Reservasi
import repository.InMemoryRepo
import repository.Repository

import java.util.LinkedList
import java.util.Queue

class ReservationService (
    private val reservasiRepo: Repository<Reservasi>,
    private val anggotaRepo: Repository<Anggota>,
    private val bukuRepo: Repository<Buku>
){
    private val antrianBuku: MutableMap<String, Queue<String>> = mutableMapOf()

    fun buatReservasi(idAnggota: String, idBuku: String){
        val anggota = anggotaRepo.findById(idAnggota)
        val buku = bukuRepo.findById(idBuku)

        if (anggota == null || buku == null) {
            println("Error: Gagal Reservasi, data anggota dan buku tidak valid")
            return
        }

        val antrianSaatIni = antrianBuku[idBuku]
        if (antrianSaatIni != null && antrianSaatIni.contains(idAnggota)) {
            println("Info: ${anggota.nama} sudah ada dalam antrian untuk buku ini")
            return
        }

        val reservasi = Reservasi(
            id = "R-${System.currentTimeMillis()}",
            idAnggota = idAnggota,
            idBuku = idBuku
        )

        val queue = antrianBuku.getOrPut(idBuku) { LinkedList() }
        queue.add(idAnggota)
        println("Reservasi berhasil: ${anggota.nama} [${antrianSaatIni?.size ?: 0 + 1}] masuk antrian untuk ${buku.judul}.")
    }
    fun prosesAntrianBerikutnya(idBuku: String) {
        val queue = antrianBuku[idBuku]

        if (queue != null && queue.isNotEmpty()) {
            val idAnggotaBerikutnya = queue.poll()

            val anggota = anggotaRepo.findById(idAnggotaBerikutnya)
            val buku = bukuRepo.findById(idBuku)

            val catatanReservasi = reservasiRepo.findAll().find {
                it.idAnggota == idAnggotaBerikutnya &&
                        it.idBuku == idBuku &&
                        it.status == "Menunggu"
            }

            if (catatanReservasi != null) {
                catatanReservasi.ubahStatus("Ditawarkan")
                reservasiRepo.save(catatanReservasi)
            }

            println("🔔 (Skenario 6) NOTIFIKASI: Buku '${buku?.judul}' sekarang tersedia untuk ${anggota?.nama} (pengantre pertama).")
        }
    }

    fun getDaftarAntrianReservasiAktif(): Map<String, Queue<String>> {
        return antrianBuku.filter {it.value.isNotEmpty()}
    }

    fun getSemuaHistoryReservasi(): List<Reservasi> {
        return reservasiRepo.findAll()
    }
}