package model

class Anggota(
    val id: String,
    val nama: String,
    val tier: String,
    var statusAktif: Boolean
) {
    override fun toString(): String {
        val status = if (statusAktif) "Aktif" else "Nonaktif"
        return "👤 $nama [$tier] - $status"
    }
}
