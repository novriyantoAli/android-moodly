package net.coblos.moodly.ui.modal
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import net.coblos.moodly.ui.theme.MoodlyColors

@Composable
fun GlobalErrorModal() {
    // Mengecek apakah modal diset aktif oleh ModalManager
    if (!ModalManager.isVisible) return

    val data = ModalManager.currentModalData ?: return

    Dialog(
        onDismissRequest = { ModalManager.dismissModal() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Memungkinkan kustomisasi lebar kontainer penuh
        )
    ) {
        // Lapisan Backdrop Blur/Dim bawaan dialog
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp), // Sesuai p-container-padding-mobile
            contentAlignment = Alignment.Center
        ) {
            // Konten Dialog (Card Luar)
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .widthIn(max = 440.dp) // max-w-md w-full
                    .clip(RoundedCornerShape(16.dp)) // rounded-2xl
                    .background(MoodlyColors.OnPrimary)
                    .border(
                        1.dp,
                        MoodlyColors.OutlineVariant.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(32.dp), // p-8
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp) // space-y-6
            ) {
                // Sesi Atas: Icon & Text Informasi
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp) // space-y-4
                ) {
                    // Lingkaran Icon Error
                    Box(
                        modifier = Modifier
                            .size(64.dp) // w-16 h-16
                            .background(MoodlyColors.ErrorContainer, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Emoji/Icon Error alternatif dari Material Symbol
                        Text(
                            text = "⚠️",
                            fontSize = 24.sp
                        )
                    }

                    // Tulisan Judul & Deskripsi
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp), // space-y-2
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = data.title,
                            fontSize = 24.sp, // font-headline-md
                            fontWeight = FontWeight.SemiBold,
                            color = MoodlyColors.OnSurface,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = data.description,
                            fontSize = 16.sp, // text-body-md
                            color = MoodlyColors.OnSurfaceVariant,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.SansSerif,
                            lineHeight = 24.sp
                        )
                    }
                }

                // Sesi Bawah: Action Button
                Button(
                    onClick = {
                        data.onConfirm()
                        ModalManager.dismissModal() // otomatis menutup modal saat ditekan
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MoodlyColors.SecondaryContainer,
                        contentColor = MoodlyColors.OnSecondaryContainer
                    ),
                    shape = RoundedCornerShape(9999.dp) // rounded-full
                ) {
                    Text(
                        text = data.buttonText,
                        fontSize = 14.sp, // text-label-md
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}