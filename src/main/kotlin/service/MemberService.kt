package service

import model.Anggota
import repository.Repository

class MemberService(private val anggotaRepo: Repository<Anggota>) {
    private val validTiers = listOf("REGULAR", "PREMIUM", "STAFF")

    // Counter untuk Autonumber ID
    private var counterAnggota = 1

    private fun generateAnggotaId(): String {
        val id = "AG-${String.format("%04d", counterAnggota)}"
        counterAnggota++
        return id
    }

    fun tambahAnggota(
        // 'id' dihapus
        nama: String,
        tier: String,
        statusAktif: Boolean = true
    ) {
        val idOtomatis = generateAnggotaId()

        val tierUpper = tier.uppercase()
        if (tierUpper !in validTiers) {
            println("❌ Error: Tier '$tier' tidak valid. Gunakan: REGULAR, PREMIUM, atau STAFF.")
            return
        }

        val anggotaBaru = Anggota(
            id = idOtomatis,
            nama = nama,
            tier = tierUpper,
            statusAktif = statusAktif
        )

        anggotaRepo.save(anggotaBaru)
        println("✅ Anggota berhasil ditambahkan: ${anggotaBaru.nama} (ID: $idOtomatis)")
    }

    fun ubahStatusAnggota(id: String, statusAktifBaru: Boolean) {
        val anggota = anggotaRepo.findById(id)

        if (anggota == null) {
            println("❌ Error: Anggota dengan ID $id tidak ditemukan")
            return
        }

        anggota.statusAktif = statusAktifBaru
        anggotaRepo.save(anggota)

        val statusText = if (statusAktifBaru) "Aktif" else "Nonaktif"
        println("🔄 Status untuk '${anggota.nama}' diperbarui menjadi $statusText")
    }

    fun hapusAnggota(id: String) {
        val berhasil = anggotaRepo.delete(id)
        if (berhasil) {
            println("🗑️ Anggota dengan ID: $id berhasil dihapus")
        } else {
            println("❌ Error: Anggota dengan ID: $id tidak ditemukan")
        }
    }

    fun getAnggotaById(id: String): Anggota? {
        val anggota = anggotaRepo.findById(id)
        if (anggota == null) {
            println("ℹ️ Info: Anggota dengan ID: $id Tidak Ditemukan")
        }
        return anggota
    }

    fun lihatSemuaAnggota(): List<Anggota> {
        return anggotaRepo.findAll()
    }
}