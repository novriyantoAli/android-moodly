package net.coblos.moodly.presentation.login

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import net.coblos.moodly.presentation.navigation.Screen
import net.coblos.moodly.ui.modal.ModalManager
import net.coblos.moodly.ui.theme.MoodlyColors

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val infinityTransition = rememberInfiniteTransition(label = "FloatAnimation")
    val translateY by infinityTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "TranslateY"
    )

    val loginState by viewModel.loginState.collectAsState()

    when (loginState) {
        is LoginState.Loading -> CircularProgressIndicator()
        is LoginState.Success -> {
            // Navigate to Home screen upon successful login
            LaunchedEffect(Unit) {
                navController.navigate(Screen.Home.route) {
                    // Avoid building up a large stack of destinations when logging in
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }
        is LoginState.Error -> {
            ModalManager.showModal(
                title = "Login Fail",
                description = (loginState as LoginState.Error).message,
                buttonText = "OK",
                onConfirm = {
                    viewModel.resetLoginState()

                    ModalManager.dismissModal()
                }
            )
        }
        LoginState.Idle -> { /* Do nothing in Idle state */ }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MoodlyColors.Background),
        contentAlignment = Alignment.Center
    ) {
        // --- 1. Atmospheric Background Elements (Blur Lingkaran) ---
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-100).dp)
                .size(400.dp)
                .background(MoodlyColors.PrimaryFixed.copy(alpha = 0.2f), shape = CircleShape)
                .blur(100.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-80).dp, y = 80.dp)
                .size(350.dp)
                .background(MoodlyColors.SecondaryContainer.copy(alpha = 0.3f), shape = CircleShape)
                .blur(80.dp)
        )

        // --- 2. Konten Utama Kontainer ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .widthIn(max = 440.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Brand Identity Section ---
            Column(
                modifier = Modifier
                    .offset(y = translateY.dp)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon Self Improvement
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(MoodlyColors.Primary, shape = CircleShape)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Menggunakan Text Berisi Karakter/Emoji Meditasi atau Icon Kustom jika tidak ada di material
                    Text("🧘", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Moodly",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodlyColors.Primary,
                    fontFamily = FontFamily.SansSerif
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Step into your digital sanctuary for mental clarity and emotional balance.",
                    fontSize = 16.sp,
                    color = MoodlyColors.OnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 280.dp)
                )
            }

            // --- 3. Glass Card Login Container ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    // Efek glassmorphism di-simulasikan via semi-transparent background & border putih tipis
                    .background(Color.White.copy(alpha = 0.7f))
                    .border(1.dp, Color(0xFFE0E0E0).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(32.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    // Email Field
                    Column {
                        Text(
                            text = "Email Address",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MoodlyColors.OnSurface,
                            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            placeholder = { Text("name@example.com") },
                            leadingIcon = {
                                Icon(Icons.Default.Mail, contentDescription = "Email Icon", tint = MoodlyColors.Outline)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = MoodlyColors.SurfaceContainerLow,
                                focusedBorderColor = MoodlyColors.Primary,
                                unfocusedBorderColor = MoodlyColors.OutlineVariant
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }

                    // Password Field
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Password",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MoodlyColors.OnSurface,
                                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                            )
                            Text(
                                text = "Forgot Password?",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MoodlyColors.OnSecondaryContainer,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .clickable { /* Handle Forgot Password */ }
                            )
                        }
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("••••••••") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = "Lock Icon", tint = MoodlyColors.Outline)
                            },
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle Password",
                                        tint = MoodlyColors.Outline
                                    )
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = MoodlyColors.SurfaceContainerLow,
                                focusedBorderColor = MoodlyColors.Primary,
                                unfocusedBorderColor = MoodlyColors.OutlineVariant
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }

                    // Login Button
                    Button(
                        onClick = { viewModel.login(username, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MoodlyColors.Primary,
                            contentColor = MoodlyColors.OnPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(text = "Login to Sanctuary", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }

                    // Divider
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MoodlyColors.OutlineVariant
                        )
                        Text(
                            text = "Or continue with",
                            fontSize = 14.sp,
                            color = MoodlyColors.Outline,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MoodlyColors.OutlineVariant
                        )
                    }

                    // Social Logins
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Google Login Button
                        OutlinedButton(
                            onClick = { /* Handle Google Login */ },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MoodlyColors.OutlineVariant),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MoodlyColors.OnSurface)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Tambahkan aset logo google di res/drawable Anda
                                // Image(painter = painterResource(id = R.drawable.ic_google), contentDescription = "Google")
                                Text("🌐", fontSize = 16.sp) // Fallback emoji jika icon blm siap
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Google", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Apple Login Button
                        OutlinedButton(
                            onClick = { /* Handle Apple Login */ },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MoodlyColors.OutlineVariant),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MoodlyColors.OnSurface)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🍎", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Apple", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            // --- Footer Link ---
            Row(
                modifier = Modifier.padding(top = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "New to Mindful Path? ", color = MoodlyColors.OnSurfaceVariant, fontSize = 16.sp)
                Text(
                    text = "Create an account",
                    color = MoodlyColors.Primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { /* Navigate to register */ }
                )
            }
        }
    }



//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        OutlinedTextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("Username") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = { viewModel.login(username, password) },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Login")
//        }
//
//        when (loginState) {
//            is LoginState.Loading -> CircularProgressIndicator()
//            is LoginState.Success -> {
//                // Navigate to Home screen upon successful login
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Home.route) {
//                        // Avoid building up a large stack of destinations when logging in
//                        popUpTo(Screen.Login.route) { inclusive = true }
//                    }
//                }
//            }
//            is LoginState.Error -> {
//                Text("Error: ${(loginState as LoginState.Error).message}")
//            }
//            LoginState.Idle -> { /* Do nothing in Idle state */ }
//        }
//    }
}
