package net.coblos.moodly.presentation.consultation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ConsultationListScreen(
    viewModel: ConsultationViewModel = hiltViewModel(),
    onConsultationClick: (String) -> Unit
) {
    val consultations by viewModel.consultations.collectAsState()

    LazyColumn {
        items(consultations) { consultation ->
            ListItem(
                headlineContent = { Text("Consultation ${consultation.id}") },
                supportingContent = { Text("Status: ${consultation.status}") },
                modifier = Modifier.clickable { onConsultationClick(consultation.id) }
            )
        }
    }
}
