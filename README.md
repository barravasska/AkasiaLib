# Proyek AkasiaLib: Sistem Manajemen Perpustakaan (OOP Kotlin)
**AkasiaLib** adalah aplikasi Command-Line Interface (CLI) sederhana untuk manajemen perpustakaan. Proyek ini dibuat untuk memenuhi tugas mata kuliah Pemrograman Berorientasi Objek dan diimplementasikan sepenuhnya menggunakan bahasa **Kotlin** dengan paradigma **Object-Oriented Programming (OOP)**.
Sistem ini dirancang dengan arsitektur berlapis (Model, Repository, Service) untuk memisahkan data, logika penyimpanan, dan logika bisnis. Aplikasi ini mengelola katalog buku (cetak, digital, & audio), keanggotaan, sirkulasi (peminjaman & pengembalian), sistem reservasi (antrian), dan pelaporan, dengan semua data disimpan sementara secara *in-memory*.
-----
## Kontributor Kelompok

Proyek ini dikerjakan secara berkelompok dengan pembagian tugas yang jelas:
| Nama | NIM | Peran | Deskripsi Tugas |
| :--- | :--- | :--- | :--- |
| **Salwa Tama** | 224443045 | Anggota 1: Desain & Arsitektur | Bertanggung jawab merancang struktur program dan Class Diagram (UML), mendefinisikan relasi, atribut, dan fungsi antar kelas. |
| **Faiz M Nasrullah** | 224443029 | Anggota 2: Model & Repository | Mengimplementasikan semua kelas data (Model) seperti `Buku`, `BukuCetak`, `BukuDigital`, `Anggota`, `Peminjaman`, `Reservasi`, dsb. Membuat `Repository<T>` dan `InMemoryRepo<T>` untuk abstraksi penyimpanan data. |
| **Ghifary Barra Vasska** | 224443032 | Anggota 3: Service & Logika Bisnis | Mengerjakan "otak" aplikasi di *service layer*. Mengimplementasikan `AuthService`, `CatalogService`, `CirculationService` (termasuk denda), `MemberService`, dan `ReservationService` (antrian). |
| **Muhamad Ariyq Naufal Rafif** | 224443035 | Anggota 4: CLI & Laporan | Mengimplementasikan antarmuka pengguna (CLI) di `Main.kt`. Membuat menu, navigasi, dan menghubungkan input user ke fungsi di *service layer*. Menyajikan data laporan. |

## Fitur Utama
### Fungsionalitas Wajib
<<<<<<< HEAD
* **Manajemen Buku:** Tambah, hapus, cari, dan lihat semua buku.
* **Manajemen Anggota:** Tambah, hapus, dan lihat anggota dengan 3 tier (REGULAR, PREMIUM, STAFF).
* **Polimorfisme Buku:** Sistem dapat membedakan `BukuCetak` (punya stok) dan `BukuDigital` (tanpa stok) menggunakan *inheritance* dan *polimorfisme*.
* **Sirkulasi:** Logika peminjaman (cek stok) dan pengembalian.
* **Reservasi:** Sistem antrian (`Queue`) otomatis jika stok buku cetak habis dan notifikasi saat buku tersedia.
* **Perhitungan Denda:** Menghitung denda keterlambatan untuk buku cetak.
* **Pelaporan:** Menampilkan 4 laporan: Pinjaman aktif, Top 3 buku, Total denda, dan Antrian reservasi.
=======
  * **Manajemen Buku:** Tambah, hapus, cari, dan lihat semua buku.
  * **Manajemen Anggota:** Tambah, hapus, dan lihat anggota dengan 3 tier (REGULAR, PREMIUM, STAFF).
  * **Polimorfisme Buku:** Sistem dapat membedakan `BukuCetak` (punya stok) dan `BukuDigital` (tanpa stok) menggunakan *inheritance* dan *polimorfisme*.
  * **Sirkulasi:** Logika peminjaman (cek stok) dan pengembalian.
  * **Reservasi:** Sistem antrian (`Queue`) otomatis jika stok buku cetak habis dan notifikasi saat buku tersedia.
  * **Perhitungan Denda:** Menghitung denda keterlambatan untuk buku cetak.
  * **Pelaporan:** Menampilkan 4 laporan: Pinjaman aktif, Top 3 buku, Total denda, dan Antrian reservasi.
>>>>>>> 430ff384f2247ba60581cbf14ef73ff9ce52ed69

### Fitur Tambahan (Bonus)
Proyek ini juga mengimplementasikan 5 (lima) tugas opsional:
1.  **Login Sederhana:** Implementasi `AuthService` dengan dua *role*: `ADMIN` (bisa tambah data) dan `LIBRARIAN` (hanya operasional).
2.  **Strategy Pattern:** Logika perhitungan denda dipisahkan dari `CirculationService` menggunakan *Strategy Pattern* (`DendaRegulerStrategy`, `DendaPremiumStrategy`, `DendaStaffStrategy`).
3.  **Polimorfisme Lanjutan:** Penambahan kelas `BukuAudio` dengan aturan peminjaman sendiri, memperkaya penerapan polimorfisme.
4.  **Autonumber ID:** ID untuk Buku (cth: `BK-0001`) dan Anggota (cth: `AG-0001`) dibuat secara otomatis oleh *service layer*.
5.  **Ekspor CSV:** Fitur untuk mengekspor data laporan (pinjaman, anggota) ke file `.csv` di direktori proyek.

## Teknologi yang Digunakan
<<<<<<< HEAD
* **Bahasa:** Kotlin
* **Paradigma:** Object-Oriented Programming (OOP)
* **Konsep OOP:** Abstraksi, Enkapsulasi, Inheritance, Polimorfisme.
* **Arsitektur:** 3-Layer (Model, Repository, Service) + CLI
* **Pola Desain:** Strategy Pattern, Dependency Injection (via constructor).
=======
  * **Bahasa:** Kotlin
  * **Paradigma:** Object-Oriented Programming (OOP)
  * **Konsep OOP:** Abstraksi, Enkapsulasi, Inheritance, Polimorfisme.
  * **Arsitektur:** 3-Layer (Model, Repository, Service) + CLI
  * **Pola Desain:** Strategy Pattern, Dependency Injection (via constructor).
>>>>>>> 430ff384f2247ba60581cbf14ef73ff9ce52ed69

## Struktur Proyek
Struktur folder proyek ini dirancang untuk memisahkan tanggung jawab (Separation of Concerns).
```
src/main/kotlin/
├── main/
│   └── Main.kt             # (Anggota 4) Titik masuk aplikasi dan logika CLI
├── model/                  # (Anggota 2) Class data (POJO/POCO)
│   ├── Anggota.kt
│   ├── Buku.kt             (abstract)
│   ├── BukuAudio.kt
│   ├── BukuCetak.kt
│   ├── BukuDigital.kt
│   ├── Peminjaman.kt
│   ├── Reservasi.kt
│   ├── Role.kt             (enum)
│   └── User.kt
├── repository/             # (Anggota 2) Logika penyimpanan data
│   └── InMemoryRepo.kt     (Berisi Interface Repository<T> + Class InMemoryRepo<T>)
└── service/                # (Anggota 3) "Otak" / Logika bisnis
    ├── AuthService.kt
    ├── CatalogService.kt
    ├── CirculationService.kt
    ├── DendaStrategy.kt    (Berisi Interface + 3 class Strategy)
    ├── MemberService.kt
    └── ReservationService.kt
```
-----
##  Cara Menjalankan Proyek

1.  Pastikan Anda memiliki **IntelliJ IDEA** dan **Kotlin JDK** terinstal.
2.  *Clone* repositori ini ke mesin lokal Anda.
3.  Buka proyek menggunakan IntelliJ IDEA.
4.  Biarkan Gradle/IntelliJ mengindeks dan membangun proyek.
5.  Buka file `src/main/kotlin/Main.kt`.
6.  Klik ikon 'Play' (▶️) hijau di samping fungsi `fun main()` untuk menjalankan aplikasi.
7.  Aplikasi akan berjalan di konsol terminal Anda.

-----
**Contoh Tampilan Menu Login:**
```
========================================
         🔑 SILAKAN LOGIN - AkasiaLib 🔑
========================================
Username: admin
Password: ***
```

**Contoh Tampilan Menu Utama (Admin):**
```
========================================
       📚 MENU UTAMA (ADMIN) 📚
========================================
1. ➡️  Pinjam Buku
2. ⬅️  Kembalikan Buku
3. 📊  Tampilkan Laporan
4. 📖  Lihat Semua Buku
5. 👥  Lihat Semua Anggota
--- Menu Admin ---
7. ➕ Tambah Anggota Baru
8. ➕ Tambah Buku Baru
X. 💾 Ekspor Laporan ke CSV
9. 🚪  Logout
========================================
Pilih menu:
```

**Contoh Tampilan Laporan:**
```
========================================
               📊 LAPORAN SISTEM 📊
========================================

--- 1. Daftar Pinjaman Aktif ---
ID: P-17... | Andi -> OOP di Kotlin | Jatuh Tempo: 2025-10-29
--- 2. Buku Paling Sering Dipinjam (Top 3) ---
1. [OOP di Kotlin] - Dipinjam 1 kali.
--- 3. Total Denda Terkumpul ---
Total: Rp 0.0
--- 4. Daftar Antrian Reservasi Aktif ---
[Struktur Data]: Rudi
========================================
```
<<<<<<< HEAD
## OUTPUT
![Output] (Gallery/Output/Menu Utama.PNG)
=======
>>>>>>> 430ff384f2247ba60581cbf14ef73ff9ce52ed69
