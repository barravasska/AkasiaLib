package service

interface DendaStrategy {
    fun hitungDenda(hariTerlambat: Long): Double
}

class DendaRegulerStrategy : DendaStrategy {
    private val dendaPerHari = 1000.0
    override fun hitungDenda(hariTerlambat: Long): Double {
        return hariTerlambat * dendaPerHari
    }
}

class DendaPremiumStrategy : DendaStrategy {
    private val dendaPerHari = 1000.0
    override fun hitungDenda(hariTerlambat: Long): Double {
        return (hariTerlambat * dendaPerHari) * 0.5 // Diskon 50%
    }
}

class DendaStaffStrategy : DendaStrategy {
    override fun hitungDenda(hariTerlambat: Long): Double {
        return 0.0 // Gratis
    }
}