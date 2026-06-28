package net.coblos.moodly.ui.modal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Struktur data pendukung jika isi modal ingin diubah secara dinamis nantinya
data class ModalData(
    val title: String,
    val description: String,
    val buttonText: String,
    val onConfirm: () -> Unit
)

object ModalManager {
    // State reaktif untuk mendeteksi apakah modal harus muncul atau tidak
    var isVisible by mutableStateOf(false)
        private set

    var currentModalData by mutableStateOf<ModalData?>(null)
        private set

    // Fungsi global yang bisa dipanggil dari screen manapun
    fun showModal(
        title: String,
        description: String,
        buttonText: String = "Coba Lagi",
        onConfirm: () -> Unit = {}
    ) {
        currentModalData = ModalData(title, description, buttonText, onConfirm)
        isVisible = true
    }

    fun dismissModal() {
        isVisible = false
        currentModalData = null
    }
}