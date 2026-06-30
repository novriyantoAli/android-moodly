package net.coblos.moodly.presentation.consultation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.ModeEditOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import net.coblos.moodly.data.remote.dto.ConsultationResponse
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import net.coblos.moodly.domain.model.ChatItem
import net.coblos.moodly.ui.theme.MoodlyColors
import java.time.ZonedDateTime

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationListScreen(
    viewModel: ConsultationViewModel = hiltViewModel(),
    onConsultationClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    
    val listState = rememberLazyListState()
    
    // Logic detect scroll to bottom
    LaunchedEffect(listState) {
        // Simple logic for pagination trigger
        // In a real app, use `snapshotFlow` for better accuracy
    }

    Scaffold(
        topBar = { /* ... keep top bar as is ... */ },
        floatingActionButton = {
        /* ... keep FAB as is ... */
            FloatingActionButton(
                onClick = { },
                containerColor = MoodlyColors.Primary,
                contentColor = MoodlyColors.OnPrimary,
                shape = RoundedCornerShape(16.dp) // rounded-2xl
            ) {
                Icon(Icons.Default.Edit, contentDescription = "New Message")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MoodlyColors.Background)
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- SEARCH SECTION ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Cari berdasarkan nama...") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = MoodlyColors.Outline) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MoodlyColors.SurfaceContainerLowest,
                    unfocusedContainerColor = MoodlyColors.SurfaceContainerLowest,
                    focusedBorderColor = MoodlyColors.Primary,
                    unfocusedBorderColor = MoodlyColors.OutlineVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
                // ... rest of search field ...
            )

            // --- CATEGORY CHIPS ---
            val categories = listOf("Semua" to null, "Aktif" to "ACTIVE", "Ditutup" to "CLOSED")
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (label, status) ->
                    val isSelected = selectedStatus == status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(if (isSelected) MoodlyColors.Primary else MoodlyColors.Surface.copy(alpha = 0.5f))
                            .clickable { viewModel.setStatus(status) }
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(text = label, color = if (isSelected) MoodlyColors.OnPrimary else MoodlyColors.OnSurfaceVariant, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // --- CHAT LIST ---
            when (val state = uiState) {
                // 1. TAMPILAN LOADING (Sesuai tema tenang & minimalis)
                is ConsultationUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f), // Mengisi sebagian layar agar pas di tengah main content
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                color = MoodlyColors.Primary,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(44.dp)
                            )
                            Text(
                                text = "Memuat lembaran ketenangan...",
                                fontSize = 14.sp,
                                color = MoodlyColors.OnSurfaceVariant.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // 2. TAMPILAN ERROR (Menggunakan gaya bento card berwarna error container lembut)
                is ConsultationUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MoodlyColors.ErrorContainer.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Lingkaran Icon Gagal
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(MoodlyColors.ErrorContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("⚠️", fontSize = 24.sp)
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Gagal Memuat Percakapan",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MoodlyColors.Error
                                )
                                Text(
                                    text = state.message,
                                    fontSize = 14.sp,
                                    color = MoodlyColors.OnSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }

                            // Tombol Coba Lagi bawaan tema
                            TextButton(
                                onClick = { viewModel.loadMore() }, // Sesuaikan fungsi di ViewModel Anda
                                colors = ButtonDefaults.textButtonColors(contentColor = MoodlyColors.Primary)
                            ) {
                                Text("Ketuk untuk Mencoba Lagi", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                // 3. TAMPILAN SUKSES (LazyColumn dengan list data)
                is ConsultationUiState.Success -> {
                    if (state.data.isEmpty()) {
                        // Tampilan jika data kosong (opsional tapi bagus untuk user experience)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Belum ada riwayat konsultasi.",
                                color = MoodlyColors.OnSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        // Ganti LazyColumn menjadi item yang rapi
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            state.data.forEach { chat ->
                                ConsultationItemUI(
                                    chat = chat,
                                    onClick = { onConsultationClick(chat.conversationId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun parseRelativeTime(createdAtStr: String?): String {
    if (createdAtStr.isNullOrBlank()) return "-"
    return try {
        // 1. Bersihkan string tanggal agar formatnya seragam (menghapus pecahan mikrodetik jika ada)
        // Regex ini mengubah "2026-06-26T23:29:56.835832+08:00" menjadi "2026-06-26T23:29:56+0800"
        val cleanFormat = createdAtStr
            .replace(Regex("\\.\\d+"), "") // Menghapus .835832
            .replace(Regex("(\\+\\d\\d):(\\d\\d)$"), "$1$2") // Mengubah +08:00 menjadi +0800
            .replace("Z", "+0000")

        // 2. Gunakan pattern universal yang mengenali offset timezone (+0800)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())

        val createdAtDate: Date = sdf.parse(cleanFormat) ?: return createdAtStr
        val now = Date()

        val diffInMillies = now.time - createdAtDate.time
        val diffInMinutes = diffInMillies / (1000 * 60)
        val diffInHours = diffInMillies / (1000 * 60 * 60)
        val diffInDays = diffInMillies / (1000 * 60 * 60 * 24)

        when {
            diffInMinutes < 1 -> "Baru saja"
            diffInMinutes < 60 -> "$diffInMinutes mnt lalu"
            diffInHours < 24 -> "$diffInHours jam lalu"
            diffInDays == 1L -> "Kemarin"
            diffInDays < 7 -> "$diffInDays hari lalu"
            else -> {
                val outputFormat = SimpleDateFormat("d MMM yyyy", Locale("id", "ID"))
                outputFormat.format(createdAtDate)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // Jika gagal, tampilkan potongan tanggal depannya saja (YYYY-MM-DD) agar UI tidak rusak
        if (createdAtStr.length >= 10) createdAtStr.substring(0, 10) else createdAtStr
    }
}
//@Composable
//fun ConsultationItemUI(chat: ConsultationResponse, onClick: () -> Unit) {
//    val interactionSource = remember { MutableInteractionSource() }
//    val isPressed by interactionSource.collectIsPressedAsState()
//    val scale by animateFloatAsState(targetValue = if (isPressed) 0.98f else 1.0f, label = "clickAnimation")
//
//    val statusUpper = chat.status.uppercase()
//    val isActiveStatus = statusUpper == "ACTIVE"
//
//    val initialName = chat.psychologist.name.trim().firstOrNull()?.toString()?.uppercase() ?: "?"
//
//    val (statusBg, statusText, accentColor) = when (statusUpper) {
//        "ACTIVE" -> listOf(Color(0xFFE8F5E9), Color(0xFF2E7D32), MoodlyColors.Primary)
//        "WAITING" -> listOf(Color(0xFFFFF3E0), Color(0xFFE65100), Color(0xFFF2A104))
//        else -> listOf(Color(0xFFF5F5F5), Color(0xFF616161), MoodlyColors.OutlineVariant)
//    }
//
//    val cardBackgroundColor = if (isActiveStatus) {
//        Color.White
//    } else {
//        MoodlyColors.SurfaceContainerLow.copy(alpha = 0.8f)
//    }
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(IntrinsicSize.Min) // 1. SOLUSI: Memaksa Row mengukur tinggi minimum berdasarkan konten teks internalnya
//            .scale(scale)
//            .then(
//                if (isActiveStatus) {
//                    Modifier.shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp))
//                } else Modifier
//            )
//            .clip(RoundedCornerShape(16.dp))
//            .background(cardBackgroundColor)
//            .clickable(
//                interactionSource = interactionSource,
//                indication = LocalIndication.current,
//                onClick = onClick
//            )
//            .border(
//                width = 1.dp,
//                color = if (isActiveStatus) MoodlyColors.Primary.copy(alpha = 0.15f) else MoodlyColors.OutlineVariant.copy(alpha = 0.4f),
//                shape = RoundedCornerShape(16.dp)
//            ),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // 2. Aksen garis vertikal sekarang akan mengikuti tinggi teks secara presisi (tidak melar lagi)
//        Box(
//            modifier = Modifier
//                .width(6.dp)
//                .fillMaxHeight()
//                .background(accentColor)
//        )
//
//        Row(
//            modifier = Modifier
//                .weight(1f)
//                .padding(vertical = 16.dp, horizontal = 14.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // --- 1. AVATAR MONOGRAM ---
//            Box(
//                modifier = Modifier
//                    .size(52.dp)
//                    .clip(CircleShape)
//                    .background(MoodlyColors.Primary.copy(alpha = 0.08f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = initialName,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = MoodlyColors.Primary
//                )
//            }
//
//            // --- 2. DETAIL INFO SECTIONS ---
//            Column(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Text(
//                    text = chat.psychologist.name,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = MoodlyColors.OnSurface,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Text(
//                    text = "Atlit: ${chat.participant.name}",
//                    fontSize = 13.sp,
//                    color = MoodlyColors.OnSurfaceVariant.copy(alpha = 0.8f),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Spacer(modifier = Modifier.height(2.dp))
//
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(6.dp))
//                        .background(statusBg)
//                        .padding(horizontal = 8.dp, vertical = 4.dp)
//                ) {
//                    Text(
//                        text = statusUpper,
//                        fontSize = 11.sp,
//                        fontWeight = FontWeight.ExtraBold,
//                        color = statusText,
//                        letterSpacing = 0.5.sp
//                    )
//                }
//            }
//
//            // --- 3. WAKTU RELATIF SISTEM ---
//            Text(
//                text = parseRelativeTime(chat.created_at),
//                fontSize = 12.sp,
//                fontWeight = if (isActiveStatus) FontWeight.Bold else FontWeight.Normal,
//                color = if (isActiveStatus) MoodlyColors.Primary else MoodlyColors.Outline,
//                modifier = Modifier.align(Alignment.Top)
//            )
//        }
//    }
//}
@Composable
fun ConsultationItemUI(chat: ConsultationResponse, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.98f else 1.0f, label = "clickAnimation")

    val statusUpper = chat.status.uppercase()
    val isActiveStatus = statusUpper == "ACTIVE"

    val initialName = chat.psychologist.name.trim().firstOrNull()?.toString()?.uppercase() ?: "?"

    // Konfigurasi visual garis tepi kiri (Accent Indicator)
    val accentColor = when (statusUpper) {
        "ACTIVE" -> MoodlyColors.Primary
        "WAITING" -> Color(0xFFF2A104) // Oranye tegas
        else -> MoodlyColors.OutlineVariant     // CLOSED
    }

    // Adaptasi warna latar kontainer murni dari logika HTML
    // Jika ACTIVE (atau unread pada web), menggunakan warna putih solid + shadow agar stand out
    val cardBackgroundColor = if (isActiveStatus) {
        Color.White
    } else {
        MoodlyColors.SurfaceContainerLow.copy(alpha = 0.6f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Mencegah pemelaran tinggi konten yang berlebihan
            .scale(scale)
            .then(
                if (isActiveStatus) {
                    Modifier.shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                } else Modifier
            )
            .clip(RoundedCornerShape(16.dp))
            .background(cardBackgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .border(
                width = 1.dp,
                color = if (isActiveStatus) MoodlyColors.Primary.copy(alpha = 0.15f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // AKSEN TEMBOK VERTIKAL KIRI (Sesuai status)
        Box(
            modifier = Modifier
                .width(6.dp)
                .fillMaxHeight()
                .background(accentColor)
        )

        // STRUKTUR UTAMA ISI KONTEN (Penyelarasan Horizontal)
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- 1. AVATAR MONOGRAM (Inisial Nama Psikolog) ---
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MoodlyColors.Primary.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initialName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodlyColors.Primary
                )
            }

            // --- 2. INFORMASI DETAIL CHAT (NAMA, SUB-PESAN, BADGE) ---
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Baris Atas: Nama Psikolog & Waktu Relatif
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.psychologist.name,
                        fontSize = 16.dp.value.sp, // Menyesuaikan set text-body-md (16px)
                        fontWeight = FontWeight.Bold,
                        color = MoodlyColors.OnSurface,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = parseRelativeTime(chat.createdAt),
                        fontSize = 12.sp,
                        fontWeight = if (isActiveStatus) FontWeight.Bold else FontWeight.Normal,
                        color = if (isActiveStatus) MoodlyColors.Primary else MoodlyColors.Outline
                    )
                }

                // Baris Tengah: Teks Identitas Atlet Pendukung
                Text(
                    text = "Atlit: ${chat.participant.name}",
                    fontSize = 13.sp,
                    color = MoodlyColors.OnSurfaceVariant.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Baris Bawah: Isi Pesan Terakhir (Mengadopsi elemen visual ikon HTML)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Kondisi simulasi visual murni dari HTML Anda
                        if (statusUpper == "CLOSED") {
                            // Tampilkan icon centang dua jika status selesai (Chat 2 pada HTML)
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MoodlyColors.Outline
                            )
                        } else if (statusUpper == "WAITING") {
                            // Tampilkan icon mic pesan suara jika status menunggu (Chat 3 pada HTML)
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MoodlyColors.Outline
                            )
                        }

                        Text(
                            text = when (statusUpper) {
                                "ACTIVE" -> "Bagaimana perasaan Anda setelah sesi meditasi tadi pagi?"
                                "WAITING" -> "Pesan suara (0:45)"
                                else -> "Sampai jumpa di jadwal konsultasi berikutnya."
                            },
                            fontSize = 14.sp,
                            fontWeight = if (isActiveStatus) FontWeight.SemiBold else FontWeight.Normal,
                            fontStyle = if (statusUpper == "WAITING") FontStyle.Italic else FontStyle.Normal,
                            color = MoodlyColors.OnSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // --- 3. UNREAD MESSAGE BADGE (Hanya Muncul Jika Status ACTIVE / Sesuai Chat 1 HTML) ---
                    if (isActiveStatus) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(MoodlyColors.Primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "2", // Angka jumlah pesan belum dibaca dari HTML
                                color = MoodlyColors.OnPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}