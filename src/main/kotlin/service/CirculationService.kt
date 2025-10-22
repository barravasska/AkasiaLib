package service

import model.*
import repository.Repository
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CirculationService(
    private val bukuRepo: Repository<Buku>,
    private val anggotaRepo: Repository<Anggota>,
    private val peminjamanRepo: Repository<Peminjaman>,
    private val reservationService: ReservationService
) {

    var totalDendaTerkumpul: Double = 0.0
        private set

    fun pinjamBuku(idAnggota: String, idBuku: String) {
        val anggota = anggotaRepo.findById(idAnggota)
        val buku = bukuRepo.findById(idBuku)

        if (anggota == null || !anggota.statusAktif) {
            println("❌ Error: Anggota tidak valid atau tidak aktif.")
            return
        }
        if (buku == null) {
            println("❌ Error: Buku tidak ditemukan.")
            return
        }

        when (buku) {
            is BukuCetak -> {
                if (buku.stok > 0) {
                    buku.stok -= 1
                    bukuRepo.save(buku)
                    buatCatatanPeminjaman(anggota, buku)
                    println("✅ (Skenario 2) Peminjaman buku cetak '${buku.judul}' oleh ${anggota.nama} berhasil.")
                } else {
                    println("ℹ️ (Skenario 4) Peminjaman gagal: Stok buku '${buku.judul}' habis.")
                    reservationService.buatReservasi(idAnggota, idBuku)
                }
            }

            is BukuDigital, is BukuAudio -> { // Buku Audio disamakan dgn Digital
                val tipe = if (buku is BukuDigital) "digital" else "audio"
                buatCatatanPeminjaman(anggota, buku)
                println("✅ Peminjaman buku $tipe '${buku.judul}' oleh ${anggota.nama} berhasil.")
            }
        }
    }

    fun kembalikanBuku(idPeminjaman: String) {
        val peminjaman = peminjamanRepo.findById(idPeminjaman)

        if (peminjaman == null || !peminjaman.isAktif) {
            println("❌ Error: Peminjaman tidak ditemukan atau sudah selesai.")
            return
        }

        val buku = bukuRepo.findById(peminjaman.idBuku)
        val anggota = anggotaRepo.findById(peminjaman.idAnggota)

        if (buku == null || anggota == null) {
            println("❌ Error: Data buku atau anggota tidak ditemukan.")
            return
        }

        val tanggalKembaliHariIni = LocalDate.now()
        peminjaman.tandaiKembali(tanggalKembaliHariIni) // Kirim tanggal ke model

        if (buku is BukuCetak) {
            val hariTerlambat = ChronoUnit.DAYS.between(peminjaman.tanggalJatuhTempo, tanggalKembaliHariIni)

            if (hariTerlambat > 0) {
                // --- STRATEGY PATTERN ---
                // 1. Pilih Strategi
                val strategiDenda: DendaStrategy = when (anggota.tier.uppercase()) {
                    "PREMIUM" -> DendaPremiumStrategy()
                    "STAFF" -> DendaStaffStrategy()
                    else -> DendaRegulerStrategy()
                }
                // 2. Hitung Denda
                val denda = strategiDenda.hitungDenda(hariTerlambat)
                // --- END STRATEGY ---

                totalDendaTerkumpul += denda
                println("🚫 (Skenario 5) Pengembalian terlambat $hariTerlambat hari. Denda: Rp $denda")
            } else {
                println("👍 Pengembalian tepat waktu. Tidak ada denda.")
            }

            buku.stok += 1
            bukuRepo.save(buku)
            println("ℹ️ Stok '${buku.judul}' telah dikembalikan. Stok sekarang: ${buku.stok}")
            reservationService.prosesAntrianBerikutnya(buku.id)

        } else if (buku is BukuDigital || buku is BukuAudio) {
            val tipeBuku = if (buku is BukuDigital) "digital" else "audio"
            println("👍 Pengembalian buku $tipeBuku '${buku.judul}' berhasil.")
        }

        peminjamanRepo.save(peminjaman)
    }

    private fun buatCatatanPeminjaman(anggota: Anggota, buku: Buku) {
        val lamaPinjamHari = when (anggota.tier.uppercase()) {
            "PREMIUM", "STAFF" -> 14L
            "REGULAR" -> 7L
            else -> 7L
        }

        // Aturan khusus: Digital/Audio selalu 14 hari
        val finalLamaPinjam = if (buku is BukuDigital || buku is BukuAudio) 14L else lamaPinjamHari

        val tanggalPinjam = LocalDate.now()
        val tanggalJatuhTempo = tanggalPinjam.plusDays(finalLamaPinjam)

        val peminjaman = Peminjaman(
            id = "P-${System.currentTimeMillis()}", // ID unik
            idBuku = buku.id,
            idAnggota = anggota.id,
            tanggalPinjam = tanggalPinjam,
            tanggalJatuhTempo = tanggalJatuhTempo
        )
        peminjamanRepo.save(peminjaman)
    }

    fun getDaftarPinjamanAktif(): List<Peminjaman> {
        return peminjamanRepo.findAll().filter { it.isAktif }
    }
}