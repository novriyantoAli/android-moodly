package net.coblos.moodly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.coblos.moodly.data.local.pref.UserPreferencesDataSource
import net.coblos.moodly.presentation.navigation.MainScreen
import net.coblos.moodly.ui.modal.GlobalErrorModal
import net.coblos.moodly.ui.theme.MoodlyTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesDataSource: UserPreferencesDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MoodlyTheme {
//                MainScreen(userPreferencesDataSource = userPreferencesDataSource)
                Box(modifier = Modifier.fillMaxSize()) {
                    // Layar utama aplikasi Anda
                    MainScreen(userPreferencesDataSource = userPreferencesDataSource)

                    // Modal stand-by di layer paling atas aplikasi
                    GlobalErrorModal()
                }
            }
        }
    }
}
