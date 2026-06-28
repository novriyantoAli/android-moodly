package net.coblos.moodly.presentation.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import net.coblos.moodly.data.local.pref.UserPreferences
import net.coblos.moodly.presentation.navigation.Screen
import net.coblos.moodly.domain.model.MoodEntry
import net.coblos.moodly.ui.theme.MoodlyColors

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
//    val moodList by viewModel.moodHistory.collectAsState(initial = emptyList())

    var selectedMood by remember { mutableStateOf("Senang") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MoodlyColors.SurfaceContainer)
                        ) {
                            // Sesuai data-alt: Gambar Profile Sarah
                            Text("👩", modifier = Modifier.align(Alignment.Center), fontSize = 20.sp)
                        }
                        Text(
                            "Moodly",
                            color = MoodlyColors.Primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MoodlyColors.Primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MoodlyColors.Background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Action Tambah Jurnal */ },
                containerColor = MoodlyColors.Primary,
                contentColor = MoodlyColors.OnPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.AddComment, contentDescription = "Add Comment")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MoodlyColors.Background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp) // Gap bento-stack
        ) {

            // --- WELCOME SECTION ---
            Column {
                Text(
                    text = "Halo, Sarah",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodlyColors.OnSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Semoga harimu tenang. Mari kita lihat perkembangan kesejahteraan mentalmu hari ini.",
                    fontSize = 16.sp,
                    color = MoodlyColors.OnSurfaceVariant
                )
            }

            // --- MOOD TRACKER WIDGET ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0).copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Mood Tracker", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = MoodlyColors.Primary)
                        Text("Hari ini, 24 Mei", fontSize = 14.sp, color = MoodlyColors.OnSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    val moods = listOf(
                        MoodItemData("😔", "Sedih"),
                        MoodItemData("😐", "Biasa"),
                        MoodItemData("😊", "Senang"),
                        MoodItemData("🤩", "Hebat"),
                        MoodItemData("😴", "Lelah")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        moods.forEach { mood ->
                            val isSelected = selectedMood == mood.label
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { selectedMood = mood.label }
                                    .padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) MoodlyColors.SecondaryContainer else MoodlyColors.SurfaceContainer)
                                        .border(if (isSelected) 2.dp else 0.dp, MoodlyColors.Primary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(mood.emoji, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = mood.label,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MoodlyColors.Primary else MoodlyColors.OnSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // --- BENTO GRID REPORTS ---
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // 1. Questionnaire Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.size(40.dp).background(MoodlyColors.Primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))) {
                                Icon(Icons.Default.Assignment, "Quest", modifier = Modifier.align(Alignment.Center), tint = MoodlyColors.Primary)
                            }
                            Text("Kuesioner Terakhir", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Analisis Tingkat Kecemasan (GAD-7)", fontSize = 14.sp, color = MoodlyColors.OnSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                            Text("Ringan", fontSize = 32.sp, color = MoodlyColors.Primary, fontWeight = FontWeight.Medium)
                            Text("Skor: 6/21", fontSize = 14.sp, color = MoodlyColors.OnSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.28f },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                            color = MoodlyColors.Secondary,
                            trackColor = MoodlyColors.SurfaceContainer
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MoodlyColors.OutlineVariant)
                        ) {
                            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text("Lihat Detail Laporan", color = MoodlyColors.Primary)
                                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp), tint = MoodlyColors.Primary)
                            }
                        }
                    }
                }

                // 2. Consultation Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.size(40.dp).background(MoodlyColors.Secondary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))) {
                                Icon(Icons.Default.MedicalServices, "Consult", modifier = Modifier.align(Alignment.Center), tint = MoodlyColors.Secondary)
                            }
                            Text("Konsultasi Terakhir", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(MoodlyColors.SurfaceContainer)) {
                                Text("👨‍⚕️", modifier = Modifier.align(Alignment.Center), fontSize = 24.sp)
                            }
                            Column {
                                Text("Dr. Aris Setiawan, M.Psi", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Sesi: 22 Mei 2024", fontSize = 12.sp, color = MoodlyColors.OnSurfaceVariant)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.fillMaxWidth().background(MoodlyColors.SurfaceContainer.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(16.dp)) {
                            Text("\"Lanjutkan latihan pernapasan kotak saat merasa tertekan di kantor. Fokus pada progres kecil.\"", fontSize = 16.sp, color = MoodlyColors.OnSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MoodlyColors.Primary)
                        ) {
                            Text("Jadwalkan Konsultasi")
                        }
                    }
                }
            }

            // --- TIPS OF THE DAY (GROUNDING 5-4-3-2-1) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, MoodlyColors.Primary.copy(alpha = 0.9f)),
                            startY = 0f
                        )
                    )
            ) {
                // Ilustrasi Background Alam Meditasi (Sesuai Placeholder)
                Box(modifier = Modifier.fillMaxSize().background(MoodlyColors.PrimaryContainer.copy(alpha = 0.4f)))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MoodlyColors.SecondaryContainer, modifier = Modifier.size(16.dp))
                        Text("TIPS HARI INI", color = MoodlyColors.SecondaryContainer, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Teknik Grounding 5-4-3-2-1", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Gunakan teknik ini untuk kembali ke masa kini saat merasa cemas. Identifikasi 5 benda di sekitarmu, 4 suara, 3 tekstur...",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

//    Scaffold(
//        topBar = {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("Mood History")
//                Button(onClick = { viewModel.logout() }) { // Assuming logout function exists in ViewModel
//                    Text("Logout")
//                }
//            }
//        }
//    ) {
//        LazyColumn(modifier = Modifier.padding(it)) {
//            items(moodList) {
//                MoodItem(mood = it) // Assuming MoodItem composable exists
//            }
//        }
//    }
}

// Data class untuk menampung data mood
data class MoodItemData(val emoji: String, val label: String)

@Composable
fun MoodItem(
    mood: MoodItemData,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Interaction source untuk mendeteksi status klik/tekan (active state)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Efek animasi scale down saat ditekan (active:scale-95 di Tailwind)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        label = "MoodItemScaleAnimation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Menghilangkan ripple bawaan agar sesuai gaya clean web
                onClick = onClick
            )
            .padding(4.dp)
    ) {
        // Lingkaran Emoji
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                // Menggunakan MoodlyColors sesuai tema HTML Anda
                .background(
                    if (isSelected) MoodlyColors.SecondaryContainer
                    else MoodlyColors.SurfaceContainer
                )
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = MoodlyColors.Primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = mood.emoji, fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Teks Label Mood
        Text(
            text = mood.label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MoodlyColors.Primary else MoodlyColors.OnSurfaceVariant
        )
    }
}
