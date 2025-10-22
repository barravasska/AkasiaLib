package service

import model.Buku
import model.BukuAudio
import model.BukuCetak
import model.BukuDigital
import repository.Repository

class CatalogService(val bukuRepo: Repository<Buku>) {
    private var counterBuku = 1
    private fun generateBukuId(): String {
        val id = "BK-${String.format("%04d", counterBuku)}"
        counterBuku++
        return id
    }

    fun tambahBukuCetak(
        // 'id' dihapus
        judul: String,
        penulis: String,
        tahun: Int,
        kategori: String,
        jumlahHalaman: Int,
        stok: Int
    ) {
        val idOtomatis = generateBukuId()

        val bukuBaru = BukuCetak(
            id = idOtomatis,
            judul = judul,
            penulis = penulis,
            tahun = tahun,
            kategori = kategori,
            jumlahHalaman = jumlahHalaman,
            stok = stok
        )
        bukuRepo.save(bukuBaru)
        println("✅ Buku cetak berhasil ditambahkan: ${bukuBaru.judul} (ID: $idOtomatis)")
    }

    fun tambahBukuDigital(
        // 'id' dihapus
        judul: String,
        penulis: String,
        tahun: Int,
        kategori: String,
        ukuranFileMb: Double,
        formatDigital: String
    ) {
        val idOtomatis = generateBukuId()
        if (formatDigital.uppercase() !in listOf("PDF", "EPUB")) {
            println("⚠️ Format $formatDigital Tidak Sesuai Standar (tetap disimpan).")
        }

        val bukuBaru = BukuDigital(
            id = idOtomatis,
            judul = judul,
            penulis = penulis,
            tahun = tahun,
            kategori = kategori,
            ukuranFileMb = ukuranFileMb,
            formatDigital = formatDigital
        )
        bukuRepo.save(bukuBaru)
        println("✅ Buku digital berhasil ditambahkan: ${bukuBaru.judul} (ID: $idOtomatis)")
    }

    fun tambahBukuAudio(
        // 'id' dihapus
        judul: String,
        penulis: String,
        tahun: Int,
        kategori: String,
        durasiMenit: Int,
        narrator: String,
        ukuranFileMb: Double
    ) {
        val idOtomatis = generateBukuId()

        val bukuBaru = BukuAudio(
            id = idOtomatis,
            judul = judul,
            penulis = penulis,
            tahun = tahun,
            kategori = kategori,
            durasiMenit = durasiMenit,
            narrator = narrator,
            ukuranFileMb = ukuranFileMb
        )
        bukuRepo.save(bukuBaru)
        println("✅ Buku audio berhasil ditambahkan: ${bukuBaru.judul} (ID: $idOtomatis)")
    }

    fun hapusBuku(id: String) {
        val berhasil = bukuRepo.delete(id)
        if (berhasil) {
            println("🗑️ Buku dengan ID: $id berhasil dihapus")
        } else {
            println("❌ Error gagal menghapus, buku ID: $id tidak ditemukan")
        }
    }

    fun updateStokBuku(id: String, stokBaru: Int) {
        val buku = bukuRepo.findById(id)
        when (buku) {
            is BukuCetak -> {
                buku.stok = stokBaru
                bukuRepo.save(buku)
                println("🔄 Stok untuk buku cetak: ${buku.judul} sudah diperbarui menjadi $stokBaru")
            }
            is BukuDigital, is BukuAudio -> {
                println("❌ Error: Buku digital/audio tidak memiliki stok fisik.")
            }
            null -> {
                println("❌ Error: Buku dengan ID $id tidak ditemukan.")
            }
            else -> {
                println("❌ Error: Tipe buku tidak dikenal.")
            }
        }
    }

    fun lihatSemuaBuku(): List<Buku> {
        return bukuRepo.findAll()
    }

    fun cariBukuByJudul(query: String): List<Buku> {
        return bukuRepo.findAll().filter {
            it.judul.contains(query, ignoreCase = true)
        }
    }

    fun getBukuById(id: String): Buku? {
        return bukuRepo.findById(id)
    }
}