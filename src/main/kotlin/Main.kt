package main

import model.*
import service.*
import repository.InMemoryRepo
import repository.Repository // Impor Interface
import java.io.File
import java.time.LocalDate

val anggotaRepo: Repository<Anggota> = InMemoryRepo { it.id }
val bukuRepo: Repository<Buku> = InMemoryRepo { it.id }
val peminjamanRepo: Repository<Peminjaman> = InMemoryRepo { it.id }
val reservasiRepo: Repository<Reservasi> = InMemoryRepo { it.id }
val userRepo: Repository<User> = InMemoryRepo { it.id }

val memberService = MemberService(anggotaRepo)
val catalogService = CatalogService(bukuRepo)
val reservationService = ReservationService(reservasiRepo, anggotaRepo, bukuRepo)
val circulationService = CirculationService(bukuRepo, anggotaRepo, peminjamanRepo, reservationService)
val authService = AuthService(userRepo)


fun main() {
    seedData()
    while (true) {
        val loggedInUser = showLoginScreen()

        if (loggedInUser == null) {
            println("👋 Menutup aplikasi. Sampai jumpa!")
            break
        }
        println("\n🎉 Login Berhasil! Selamat Datang, ${loggedInUser.username} (${loggedInUser.role})")
        showAppMenu(loggedInUser)
        println("🔒 Anda telah logout. Silakan login kembali.")
    }
}

fun seedData() {
    println("--- (Skenario 1) Memuat Data Awal ---")

    authService.register("U-001", "admin", "admin123", Role.ADMIN)
    authService.register("U-002", "pustakawan", "lib123", Role.LIBRARIAN)

    memberService.tambahAnggota("Andi", "REGULAR", statusAktif = true)
    memberService.tambahAnggota("Dina", "PREMIUM", statusAktif = true)
    memberService.tambahAnggota("Rudi", "STAFF", statusAktif = true)

    catalogService.tambahBukuCetak("OOP di Kotlin", "Budi", 2024, "Pemrograman", 300, 2) // BK-0001
    catalogService.tambahBukuCetak("Dasar Algoritma", "Citra", 2022, "Pemrograman", 250, 1) // BK-0002
    catalogService.tambahBukuCetak("Struktur Data", "Doni", 2023, "Pemrograman", 400, 0) // BK-0003
    catalogService.tambahBukuDigital("Web Development", "Eka", 2024, "Web", 15.5, "PDF") // BK-0004
    catalogService.tambahBukuDigital("Mobile Apps", "Fani", 2025, "Mobile", 22.0, "EPUB") // BK-0005

    println("--- Data Awal Selesai Dimuat ---\n")
}

fun showLoginScreen(): User? {
    while (true) {
        println("\n" + "=".repeat(40))
        println("         🔑 SILAKAN LOGIN - AkasiaLib 🔑")
        println("=".repeat(40))
        print("Username: ")
        val username = readlnOrNull().orEmpty()
        print("Password: ")
        val password = readlnOrNull().orEmpty()

        if (username.isBlank() && password.isBlank()) {
            print("Ketik 'keluar' untuk menutup aplikasi, atau Enter untuk login: ")
            if (readlnOrNull().equals("keluar", ignoreCase = true)) {
                return null
            }
            continue
        }

        val user = authService.login(username, password)
        if (user != null) {
            return user
        } else {
            println("❌ Login GAGAL. Username atau password salah. Coba lagi.")
        }
    }
}

fun showAppMenu(user: User) {
    while (true) {
        println("\n" + "=".repeat(40))
        println("       📚 MENU UTAMA (${user.role}) 📚")
        println("=".repeat(40))
        println("1. ➡️  Pinjam Buku")
        println("2. ⬅️  Kembalikan Buku")
        println("3. 📊  Tampilkan Laporan")
        println("4. 📖  Lihat Semua Buku")
        println("5. 👥  Lihat Semua Anggota")

        if (user.role == Role.ADMIN) {
            println("--- Menu Admin ---")
            println("7. ➕ Tambah Anggota Baru")
            println("8. ➕ Tambah Buku Baru")
        }

        println("X. 💾 Ekspor Laporan ke CSV")
        println("9. 🚪  Logout")
        println("=".repeat(40))
        print("Pilih menu: ")

        when (readlnOrNull()?.uppercase()) {
            "1" -> menuPinjamBuku()
            "2" -> menuKembalikanBuku()
            "3" -> menuTampilkanLaporan()
            "4" -> menuLihatSemuaBuku()
            "5" -> menuLihatSemuaAnggota()

            "7" -> if (user.role == Role.ADMIN) menuTambahAnggota() else println("❌ Aksi dilarang. Hanya untuk ADMIN.")
            "8" -> if (user.role == Role.ADMIN) menuTambahBuku() else println("❌ Aksi dilarang. Hanya untuk ADMIN.")

            "X" -> menuExportLaporan()

            "9" -> {
                authService.logout()
                return
            }
            else -> println("❌ Pilihan tidak valid.")
        }
    }
}

fun menuPinjamBuku() {
    println("\n--- ➡️  Menu Pinjam Buku ---")
    print("Masukkan ID Anggota (cth: AG-0001): ")
    val idAnggota = readlnOrNull().orEmpty()
    print("Masukkan ID Buku (cth: BK-0001): ")
    val idBuku = readlnOrNull().orEmpty()

    if (idAnggota.isBlank() || idBuku.isBlank()) {
        println("❌ ID Anggota dan ID Buku tidak boleh kosong.")
        return
    }

    circulationService.pinjamBuku(idAnggota, idBuku)
}

fun menuKembalikanBuku() {
    println("\n--- ⬅️  Menu Kembalikan Buku ---")
    println("Tips: Lihat ID Peminjaman pada menu Laporan (3)")
    print("Masukkan ID Peminjaman (cth: P-17xxxx): ")
    val idPeminjaman = readlnOrNull().orEmpty()

    if (idPeminjaman.isBlank()) {
        println("❌ ID Peminjaman tidak boleh kosong.")
        return
    }

    circulationService.kembalikanBuku(idPeminjaman)
}

fun menuTampilkanLaporan() {
    println("\n" + "=".repeat(40))
    println("               📊 LAPORAN SISTEM 📊")
    println("=".repeat(40))

    println("\n--- 1. Daftar Pinjaman Aktif ---")
    val pinjamanAktif = circulationService.getDaftarPinjamanAktif()
    if (pinjamanAktif.isEmpty()) {
        println("Tidak ada buku yang sedang dipinjam.")
    } else {
        pinjamanAktif.forEach {
            val buku = bukuRepo.findById(it.idBuku)
            val anggota = anggotaRepo.findById(it.idAnggota)
            println("ID: ${it.id} | ${anggota?.nama ?: "N/A"} -> ${buku?.judul ?: "N/A"} | Jatuh Tempo: ${it.tanggalJatuhTempo}")
        }
    }

    println("\n--- 2. Buku Paling Sering Dipinjam (Top 3) ---")
    val historiPeminjaman = peminjamanRepo.findAll()
    val frekuensiBuku = historiPeminjaman
        .groupBy { it.idBuku }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second }
        .take(3)

    if (frekuensiBuku.isEmpty()) {
        println("Belum ada buku yang pernah dipinjam.")
    } else {
        frekuensiBuku.forEachIndexed { index, (idBuku, jumlah) ->
            val buku = bukuRepo.findById(idBuku)
            println("${index + 1}. [${buku?.judul ?: "ID: $idBuku"}] - Dipinjam $jumlah kali.")
        }
    }

    println("\n--- 3. Total Denda Terkumpul ---")
    println("Total: Rp ${circulationService.totalDendaTerkumpul}")

    println("\n--- 4. Daftar Antrian Reservasi Aktif ---")
    val antrian = reservationService.getDaftarAntrianReservasiAktif()
    if (antrian.isEmpty()) {
        println("Tidak ada antrian reservasi.")
    } else {
        antrian.forEach { (idBuku, queue) ->
            val buku = bukuRepo.findById(idBuku)
            val namaPengantri = queue.mapNotNull { anggotaRepo.findById(it)?.nama }
            println("[${buku?.judul ?: "ID: $idBuku"}]: ${namaPengantri.joinToString(" -> ")}")
        }
    }
    println("=".repeat(40))
}

fun menuLihatSemuaBuku() {
    println("\n--- 📖 Daftar Semua Buku di Katalog ---")
    val semuaBuku = catalogService.lihatSemuaBuku()
    if (semuaBuku.isEmpty()) {
        println("Katalog masih kosong.")
    } else {
        semuaBuku.forEach {
            println("[${it.id}] ${it.info()}")
        }
    }
}

fun menuLihatSemuaAnggota() {
    println("\n--- 👥 Daftar Semua Anggota ---")
    val semuaAnggota = memberService.lihatSemuaAnggota()
    if (semuaAnggota.isEmpty()) {
        println("Belum ada anggota terdaftar.")
    } else {
        semuaAnggota.forEach {
            println("[${it.id}] $it")
        }
    }
}

// --- Menu Admin (Autonumber) ---

fun menuTambahAnggota() {
    println("\n--- ➕ Tambah Anggota Baru (Admin) ---")
    // ID tidak diminta (Autonumber)
    print("Nama: ")
    val nama = readlnOrNull().orEmpty()
    print("Tier (REGULAR/PREMIUM/STAFF): ")
    val tier = readlnOrNull().orEmpty()

    if (nama.isBlank() || tier.isBlank()) {
        println("❌ Data tidak boleh kosong.")
        return
    }
    memberService.tambahAnggota(nama, tier, true)
}

fun menuTambahBuku() {
    println("\n--- ➕ Tambah Buku Baru (Admin) ---")
    print("Tipe Buku (1: Cetak, 2: Digital, 3: Audio): ")
    when (readlnOrNull()) {
        "1" -> {
            print("Judul: "); val judul = readlnOrNull().orEmpty()
            print("Penulis: "); val penulis = readlnOrNull().orEmpty()
            print("Tahun: "); val tahun = readlnOrNull()?.toIntOrNull() ?: 2024
            print("Kategori: "); val kategori = readlnOrNull().orEmpty()
            print("Jml Halaman: "); val hal = readlnOrNull()?.toIntOrNull() ?: 0
            print("Stok: "); val stok = readlnOrNull()?.toIntOrNull() ?: 0
            catalogService.tambahBukuCetak(judul, penulis, tahun, kategori, hal, stok)
        }
        "2" -> {
            print("Judul: "); val judul = readlnOrNull().orEmpty()
            print("Penulis: "); val penulis = readlnOrNull().orEmpty()
            print("Tahun: "); val tahun = readlnOrNull()?.toIntOrNull() ?: 2024
            print("Kategori: "); val kategori = readlnOrNull().orEmpty()
            print("Ukuran (MB): "); val size = readlnOrNull()?.toDoubleOrNull() ?: 0.0
            print("Format (PDF/EPUB): "); val format = readlnOrNull().orEmpty()
            catalogService.tambahBukuDigital(judul, penulis, tahun, kategori, size, format)
        }
        "3" -> { // Audio
            // ID tidak diminta
            print("Judul: "); val judul = readlnOrNull().orEmpty()
            print("Penulis: "); val penulis = readlnOrNull().orEmpty()
            print("Tahun: "); val tahun = readlnOrNull()?.toIntOrNull() ?: 2024
            print("Kategori: "); val kategori = readlnOrNull().orEmpty()
            print("Durasi (menit): "); val durasi = readlnOrNull()?.toIntOrNull() ?: 0
            print("Narrator: "); val narrator = readlnOrNull().orEmpty()
            print("Ukuran (MB): "); val size = readlnOrNull()?.toDoubleOrNull() ?: 0.0
            catalogService.tambahBukuAudio(judul, penulis, tahun, kategori, durasi, narrator, size)
        }
        else -> println("❌ Pilihan tidak valid.")
    }
}

// --- Menu Ekspor CSV ---

fun menuExportLaporan() {
    println("\n--- 💾 Ekspor Laporan ke CSV ---")
    println("Pilih laporan untuk diekspor:")
    println("1. Laporan Pinjaman Aktif")
    println("2. Laporan Histori Peminjaman (Lengkap)")
    println("3. Laporan Daftar Anggota")
    print("Pilih (1-3) atau 'B' untuk Batal: ")

    when (readlnOrNull()?.uppercase()) {
        "1" -> exportPinjamanAktifCsv()
        "2" -> exportHistoriPeminjamanCsv()
        "3" -> exportAnggotaCsv()
        else -> println("ℹ️ Ekspor dibatalkan.")
    }
}

private fun exportPinjamanAktifCsv() {
    println("Mengekspor laporan pinjaman aktif...")
    val data = circulationService.getDaftarPinjamanAktif()
    if (data.isEmpty()) {
        println("Tidak ada data pinjaman aktif untuk diekspor.")
        return
    }

    val namaFile = "laporan_pinjaman_aktif.csv"
    val header = "ID_Peminjaman,ID_Buku,Judul_Buku,ID_Anggota,Nama_Anggota,Tgl_Pinjam,Tgl_Jatuh_Tempo\n"

    val dataRows = data.joinToString("\n") { pinjaman ->
        val buku = bukuRepo.findById(pinjaman.idBuku)
        val anggota = anggotaRepo.findById(pinjaman.idAnggota)
        val judulBuku = buku?.judul?.replace(",", "") ?: "N/A"
        val namaAnggota = anggota?.nama?.replace(",", "") ?: "N/A"
        "${pinjaman.id},${buku?.id ?: "N/A"},$judulBuku,${anggota?.id ?: "N/A"},$namaAnggota,${pinjaman.tanggalPinjam},${pinjaman.tanggalJatuhTempo}"
    }

    try {
        File(namaFile).writeText(header + dataRows)
        println("✅ Berhasil! Laporan disimpan di file: $namaFile")
    } catch (e: Exception) {
        println("❌ Gagal menyimpan file: ${e.message}")
    }
}

private fun exportHistoriPeminjamanCsv() {
    println("Mengekspor histori peminjaman lengkap...")
    val data = peminjamanRepo.findAll()
    if (data.isEmpty()) {
        println("Tidak ada histori peminjaman untuk diekspor.")
        return
    }

    val namaFile = "laporan_histori_lengkap.csv"
    val header = "ID_Peminjaman,ID_Buku,Judul_Buku,ID_Anggota,Nama_Anggota,Tgl_Pinjam,Tgl_Jatuh_Tempo,Tgl_Kembali,Status\n"

    val dataRows = data.joinToString("\n") { pinjaman ->
        val buku = bukuRepo.findById(pinjaman.idBuku)
        val anggota = anggotaRepo.findById(pinjaman.idAnggota)
        val judulBuku = buku?.judul?.replace(",", "") ?: "N/A"
        val namaAnggota = anggota?.nama?.replace(",", "") ?: "N/A"
        val status = if (pinjaman.isAktif) "Aktif" else "Selesai"
        val tglKembali = pinjaman.tanggalKembali?.toString() ?: "N/A"
        "${pinjaman.id},${buku?.id ?: "N/A"},$judulBuku,${anggota?.id ?: "N/A"},$namaAnggota,${pinjaman.tanggalPinjam},${pinjaman.tanggalJatuhTempo},$tglKembali,$status"
    }

    try {
        File(namaFile).writeText(header + dataRows)
        println("✅ Berhasil! Laporan disimpan di file: $namaFile")
    } catch (e: Exception) {
        println("❌ Gagal menyimpan file: ${e.message}")
    }
}

private fun exportAnggotaCsv() {
    println("Mengekspor daftar anggota...")
    val data = memberService.lihatSemuaAnggota()
    if (data.isEmpty()) {
        println("Tidak ada data anggota untuk diekspor.")
        return
    }

    val namaFile = "laporan_anggota.csv"
    val header = "ID_Anggota,Nama,Tier,Status\n"

    val dataRows = data.joinToString("\n") { anggota ->
        val namaAnggota = anggota.nama.replace(",", "")
        val status = if (anggota.statusAktif) "Aktif" else "Nonaktif"
        "${anggota.id},$namaAnggota,${anggota.tier},$status"
    }

    try {
        File(namaFile).writeText(header + dataRows)
        println("✅ Berhasil! Laporan disimpan di file: $namaFile")
    } catch (e: Exception) {
        println("❌ Gagal menyimpan file: ${e.message}")
    }
}