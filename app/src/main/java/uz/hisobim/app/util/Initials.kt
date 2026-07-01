package uz.hisobim.app.util

/**
 * Ismdan bosh harflarni oladi (RN'dagi `getInitials` ekvivalenti).
 * Ikki so'z bo'lsa har biridan bittadan, aks holda dastlabki 2 harf.
 */
fun getInitials(name: String): String {
    val parts = name.trim().split(" ").filter { it.isNotEmpty() }
    if (parts.size >= 2) {
        return (parts[0].take(1) + parts[1].take(1)).uppercase()
    }
    return name.take(2).uppercase()
}
