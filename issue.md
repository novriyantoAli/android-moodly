# Moodly - High Level Planning

## Deskripsi Project
Moodly adalah aplikasi pelacak suasana hati (mood tracker) yang memungkinkan pengguna untuk mencatat perasaan harian, melihat riwayat mood, dan mendapatkan insight dari pola emosi mereka.

## Tech Stack
Aplikasi akan dibangun menggunakan modern Android development stack:
- **Core**: Kotlin & Jetpack Compose (UI)
- **Architecture**: Clean Architecture (MVI/MVVM)
- **DI**: Hilt
- **Networking**: Retrofit & OkHttp
- **Database**: Room (Local Persistence)
- **Local Settings**: DataStore
- **Concurrency**: Coroutines & Flow
- **Image Loading**: Coil
- **Serialization**: Gson

## Struktur Project (Clean Architecture)
1. **Data Layer**: Implementasi API services, Room Database, DataStore, dan Repository.
2. **Domain Layer**: Use Cases, Business Models, dan Repository Interfaces.
3. **Presentation Layer**: ViewModels, Screen States, Composables, dan Navigation.

## Rencana Implementasi

### Tahap 1: Setup Dasar & DI
- Konfigurasi `build.gradle` dan `libs.versions.toml` (Hilt, Compose, Room, dll).
- Setup Hilt Application class dan Entry Points.
- Implementasi Base components (UI theme, Common UI components).

### Tahap 2: Data Layer (Lokal & Remote)
- Desain Schema Database (Room) untuk entitas `MoodEntry`.
- Konfigurasi Retrofit + OkHttp untuk koneksi API (jika ada).
- Implementasi DataStore untuk preferensi user (misal: Tema, Nama User).
- Implementasi Repository dengan strategi caching (Single Source of Truth).

### Tahap 3: Domain Layer
- Definisi Business Model (Mood, Emotion types).
- Implementasi Use Cases (misal: `GetMoodHistoryUseCase`, `AddMoodUseCase`).

### Tahap 4: Presentation Layer & UI
- **Navigation**: Setup Compose Navigation (Splash -> Home -> Add Mood -> Details).
- **Home Screen**: List riwayat mood dengan filter waktu.
- **Add Mood Screen**: Input pilihan emosi, catatan, dan foto (menggunakan Coil).
- **State Management**: Penggunaan ViewModel untuk mengelola UI State.

### Tahap 5: Finalisasi & Polish
- Integrasi Coil untuk loading asset gambar.
- Error handling global dan loading state.
- Unit Testing untuk logika bisnis di Domain Layer.

---
*Dokumen ini dirancang sebagai panduan high-level untuk programmer atau model AI.*
